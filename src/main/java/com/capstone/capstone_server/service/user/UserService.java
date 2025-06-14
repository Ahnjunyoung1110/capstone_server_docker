package com.capstone.capstone_server.service.user;


import com.capstone.capstone_server.dto.FcmTokenDTO;
import com.capstone.capstone_server.dto.PasswordChanngeDTO;
import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.RoleEntity;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.mapper.UserMapper;
import com.capstone.capstone_server.repository.RoleRepository;
import com.capstone.capstone_server.repository.UserRepository;
import com.capstone.capstone_server.service.HospitalService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final HospitalService hospitalService;
  private final UserMapper userMapper;
  private final RoleRepository roleRepository;

  @Autowired
  private UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      HospitalService hospitalService, UserMapper userMapper, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.hospitalService = hospitalService;
    this.userMapper = userMapper;
    this.roleRepository = roleRepository;
  }

  // uuid를 통해 Entity를 반환받는 메서드
  public UserEntity findByUuid(String uuid) {
    return userRepository.findById(uuid).orElse(null);
  }

  // 신규 생성 메서드
  public UserDTO createUser(UserDTO userDTO) {
    if (userDTO == null) { // 유저 엔티티를 받지 못한 경우
      throw new IllegalArgumentException("userEntity is null");
    }
    if (userDTO.getHospitalId() == null) {
      throw new IllegalArgumentException("hospital is null");
    }

    UserEntity userEntity = DTOToEntity(userDTO);
    if (userRepository.existsByUsername(userEntity.getUsername())) { // 동일한 ID가 DB내에 존재하는 경우
      log.warn("User with id {} already exists", userEntity.getUsername());
      throw new IllegalArgumentException("Same user id is already exists");
    }

    UserEntity saveUserEntity = UserEntity.builder()
        .username(userEntity.getUsername())
        .name(userEntity.getName())
        .password(passwordEncoder.encode(userEntity.getPassword()))
        .email(userEntity.getEmail())
        .hospital(hospitalService.getHospitalById(userDTO.getHospitalId()))
        .phoneNumber(userDTO.getPhoneNumber())
        .build();

    UserEntity responseEntity = userRepository.save(saveUserEntity);
    return userMapper.EntityToDTO(responseEntity);
  }

  // fcm토큰을 저장하는 메서드
  public void updateFcm(FcmTokenDTO fcmTokenDTO) {
    if (fcmTokenDTO == null) {
      log.warn("fcmTokenDTO is null");
      throw new IllegalArgumentException("fcmTokenDTO is null");
    }

    UserEntity userEntity = findByUuid(fcmTokenDTO.getUserId());
    if (userEntity == null) {
      log.warn("userEntity is null");
      throw new IllegalArgumentException("userEntity is null");
    }

    String currentToken = userEntity.getFcmToken();
    String newToken = fcmTokenDTO.getToken();

    if (currentToken != null && !currentToken.isEmpty() && currentToken.equals(newToken)) {
      log.warn("fcmTokenDTO is already set");
      throw new IllegalArgumentException("fcmTokenDTO is already set");
    }

    userEntity.setFcmToken(fcmTokenDTO.getToken());
    userRepository.save(userEntity);
  }

  // 해당 병원에 속한 유저를 리턴하는 메서드
  public List<UserDTO> findUserInSameHospital(String uuid) {
    UserEntity userEntity = findByUuid(uuid);
    List<UserEntity> entities = userRepository.findAllByHospital(userEntity.getHospital());

    return userMapper.EntityToDTOList(entities);
  }

  // db에 저장된 모든 유저를 리턴하는 메서드
  public List<UserDTO> findUserAll() {
    List<UserEntity> entities = userRepository.findAll();

    return userMapper.EntityToDTOList(entities);
  }
    
    /*
    로그인 할때 입력된 ID와 비밀번호가 DB내에 존재하는지 확인하는 함수
     */

  public UserEntity findByIdAndPassword(String username, String password) {
    if (username == null) {
      throw new IllegalArgumentException("userEntity is null");
    }
    if (password == null) {
      throw new IllegalArgumentException("password is null");
    }

    log.info("Find user with username {} and password {}", username, password);
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);

    // 등록된 아이디가 존재하지 않는 경우
    if (optionalUserEntity.isEmpty()) {
      throw new IllegalArgumentException("Id is wrong");
    }
    UserEntity userEntity = optionalUserEntity.get();
    // 패스워드가 일치하지 않는 경우
    if (!passwordEncoder.matches(password, userEntity.getPassword())) {
      throw new IllegalArgumentException("Password is wrong");
    }

    if (!userEntity.getValid()) {
      log.info("User {} is valid", username);
      throw new IllegalArgumentException("This account has not been authorized yet.");
    }

    return userEntity;
  }

  // 유저가 본인의 정보를 변경하는 함수.
  public UserDTO updateUser(UserDTO userDTO, String uuid) {
    if (userDTO == null) {
      throw new IllegalArgumentException("userDTO is null");
    }

    String encryptedPassword = findByUuid(uuid).getPassword();
    if (!passwordEncoder.matches(userDTO.getPassword(), encryptedPassword)) {
      throw new IllegalArgumentException("Password is wrong");
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(
        userDTO.getUsername());
    if (optionalUserEntity.isEmpty()) {
      log.warn("User with id {} does not exist", userDTO.getUsername());
    }

    UserEntity userEntity = optionalUserEntity.get();
    userEntity.setEmail(userDTO.getEmail());
    userEntity.setName(userDTO.getName());
    userEntity.setPhoneNumber(String.valueOf(userDTO.getPhoneNumber()));
    userEntity.setHospital(hospitalService.getHospitalById(userDTO.getHospitalId()));

    return userMapper.EntityToDTO(userRepository.save(userEntity));
  }

  // 운영자가 유저의 정보를 변경하는 함수
  public UserDTO updateUserByAdmin(UserDTO userDTO, String requestUuid, String uuid,
      boolean admin) {
    if (userDTO == null) {
      log.warn("userDTO is null");
      throw new IllegalArgumentException("userDTO is null");
    }
    UserEntity userEntity = findByUuid(uuid);
    if (!userEntity.getUuid().equals(userDTO.getUuid())) {
      log.warn("User with id {} does not exist", userDTO.getUuid());
      throw new IllegalArgumentException("User with id " + userDTO.getUuid() + " does not exist");
    }

    UserEntity requestEntity = findByUuid(requestUuid);
    if (!admin && !requestEntity.getHospital().equals(userEntity.getHospital())) {
      log.warn("Hospial is not same as request hospital");
      throw new IllegalArgumentException("Hospial is not same as request hospital");
    }

    userEntity.setEmail(userDTO.getEmail());
    userEntity.setName(userDTO.getName());
    userEntity.setPhoneNumber(String.valueOf(userDTO.getPhoneNumber()));

    return userMapper.EntityToDTO(userRepository.save(userEntity));
  }

  // 유저의 비밀번호를 변경하는 함수
  public void updatePw(PasswordChanngeDTO passwordChanngeDTO, String encryptedPassword,
      String username) {
    if (passwordChanngeDTO == null) {
      throw new IllegalArgumentException("passwordChanngeDTO is null");
    }

    // 입력받은 비밀번호와 토큰 비밀번호가 일치하지 않는경우
    if (!passwordEncoder.matches(encryptedPassword, passwordChanngeDTO.getCurrentPassword())) {
      throw new IllegalArgumentException("Password is wrong");
    }
    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
    if (optionalUserEntity.isEmpty()) {
      log.warn("User with id {} does not exist", username);
    }

    UserEntity userEntity = optionalUserEntity.get();
    userEntity.setPassword(passwordChanngeDTO.getNewPassword());
    userRepository.save(userEntity);

  }

  // 유저의 권한과 활성화를 설정하는 함수
  public UserDTO updateUserRole(String userUuid, Set<RoleType> roles) {
    if (userUuid == null) {
      log.warn("userId is null");
      throw new IllegalArgumentException("userId is null");
    }

    if (roles == null) {
      log.warn("role is null");
      throw new IllegalArgumentException("role is null");
    }
    if (roles.contains(RoleType.ADMIN)) {
      log.warn("admin role is set to admin");
      throw new IllegalArgumentException("admin role is set to admin");
    }

    // 유저가 존재하는지 확인
    UserEntity findUser = userRepository.findById(userUuid).orElse(null);
    if (findUser == null) {
      log.warn("User with uuid {} not found", userUuid);
      throw new IllegalArgumentException("Id is wrong");
    }

    // 해당 권한을 Entity로 가져옴
    Set<RoleEntity> rolesEntity = roleRepository.findAllByNameIn(roles);

    // 권한 설정
    findUser.setRoles(rolesEntity);
    findUser.setPrimaryRole(selectPrimaryRole(rolesEntity));
    findUser.setValid(true);

    log.info("Update user role {} into {}", findUser.getUsername(), rolesEntity);

    // 저장후 반환
    return userMapper.EntityToDTO(userRepository.save(findUser));
  }

  // 로그아웃하는 함수
  public void logOut(String uuid, String responseUuid) {
    if (uuid == null || responseUuid == null) {
      throw new IllegalArgumentException("userEntity is null");
    }
    if (uuid.equals(responseUuid)) {
      UserEntity userEntity = userRepository.findById(uuid).orElse(null);
      if (userEntity != null) {
        userEntity.setFcmToken(null);
        userRepository.save(userEntity);
      } else {
        log.warn("User with uuid {} not found", uuid);
        throw new IllegalArgumentException("Id is wrong");
      }
    }
    log.info("User with uuid {} has been logged out", uuid);

  }

  // primaryRole 지정을 위한 함수
  private RoleType selectPrimaryRole(Set<RoleEntity> roles) {
    Set<RoleType> roleTypes = roles.stream()
        .map(RoleEntity::getName)
        .collect(Collectors.toSet());

    if (roleTypes.contains(RoleType.ADMIN)) {
      return RoleType.ADMIN;
    }
    if (roleTypes.contains(RoleType.MODERATOR)) {
      return RoleType.MODERATOR;
    }
    if (roleTypes.contains(RoleType.WAREHOUSE_MANAGER)) {
      return RoleType.WAREHOUSE_MANAGER;
    }
    return RoleType.USER;
  }

  // DTO 를 Entity로 변환하는 메서드
  public UserEntity DTOToEntity(UserDTO userDTO) {
    if (userDTO == null) {
      log.warn("userDTO is null");
      throw new IllegalArgumentException("userDTO is null");
    }

    HospitalEntity hospital = hospitalService.getHospitalById(userDTO.getHospitalId());

    return UserEntity.builder()
        .email(userDTO.getEmail())
        .username(userDTO.getUsername())
        .password(userDTO.getPassword())
        .email(userDTO.getEmail())
        .phoneNumber(userDTO.getPhoneNumber())
        .name(userDTO.getName())
        .hospital(hospital)
        .build();
  }

  public UserDTO invalidUser(String uuid, String requestUuid) {
    if (uuid == null || requestUuid == null) {
      log.warn("requestUuid is null");
      throw new IllegalArgumentException("requestUuid is null");
    }

    UserEntity userEntity = userRepository.findById(uuid).orElse(null);
    UserEntity requestEntity = userRepository.findById(requestUuid).orElse(null);
    if (userEntity == null || requestEntity == null) {
      log.warn("userEntity is null");
      throw new IllegalArgumentException("userEntity is null");
    }

    if (!userEntity.getHospital().equals(requestEntity.getHospital())) {
      log.warn("hospital is not the same");
      throw new IllegalArgumentException("hospital is not the same");
    }

    if (requestEntity.getRoles().contains(RoleType.MODERATOR)) {
      log.warn("moderator is set to moderator");
      throw new IllegalArgumentException("moderator is set to moderator");
    }

    requestEntity.setValid(false);
    userRepository.save(userEntity);

    return userMapper.EntityToDTO(userEntity);
  }

  public UserDTO invalidUserByAdmin(String id) {
    log.info("invalidUserByAdmin {}", id);
    UserEntity userEntity = userRepository.findById(id).orElse(null);
    if (userEntity == null) {
      log.warn("userEntity is null");
      throw new IllegalArgumentException("userEntity is null");
    }

    userEntity.setValid(false);
    userRepository.save(userEntity);
    return userMapper.EntityToDTO(userEntity);
  }
}
