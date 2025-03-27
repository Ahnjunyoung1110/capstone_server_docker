package com.capstone.capstone_server.service;


import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.PermissionEntity;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.mapper.UserMapper;
import com.capstone.capstone_server.repository.UserRepository;
import java.util.Optional;
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
  private final PermissionService permissionService;
  private final UserMapper userMapper;

  @Autowired
  private UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, HospitalService hospitalService, UserMapper userMapper, PermissionService permissionService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.hospitalService = hospitalService;
    this.userMapper = userMapper;
    this.permissionService = permissionService;
  }

  // 신규 생성 메서드
  public UserDTO createUser(UserDTO userDTO) {
    if (userDTO == null) { // 유저 엔티티를 받지 못한 경우
      throw new IllegalArgumentException("userEntity is null");
    }
    if (userDTO.getHospital() == null) {
      throw new IllegalArgumentException("hospital is null");
    }

    UserEntity userEntity = DTOToEntity(userDTO);
    if (userRepository.existsByUsername(userEntity.getUsername())) { // 동일한 ID가 DB내에 존재하는 경우
      log.warn("User with id {} already exists", userEntity.getUsername());
      throw new IllegalArgumentException("Same user id is already exists");
    }

    String EncryptedPassword = passwordEncoder.encode(userEntity.getPassword());
    userEntity.setPassword(EncryptedPassword);

    UserEntity savedUserEntity = userRepository.save(userEntity);
    return userMapper.EntityToDTO(savedUserEntity);
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

    return userEntity;
  }


  // DTO 를 Entity로 변환하는 메서드
  public UserEntity DTOToEntity(UserDTO userDTO) {
    if (userDTO == null) {
      log.warn("userDTO is null");
      throw new IllegalArgumentException("userDTO is null");
    }

    HospitalEntity hospital = hospitalService.getHospitalById(userDTO.getHospital());
    PermissionEntity permission = permissionService.getPermission(userDTO.getPermission());


    return UserEntity.builder()
        .email(userDTO.getEmail())
        .username(userDTO.getUsername())
        .password(userDTO.getPassword())
        .email(userDTO.getEmail())
        .profession(userDTO.getProfession())
        .phoneNumber(userDTO.getPhoneNumber())
        .name(userDTO.getName())
        .hospital(hospital)
        .permission(permission)
        .build();
  }
}
