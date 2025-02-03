package com.capstone.capstone_server.service;


import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param userEntity
     * 회원가입 메서드
     */
    public UserEntity createUser(UserEntity userEntity) {
        if(userEntity== null){ // 유저 엔티티를 받지 못한 경우
            throw new IllegalArgumentException("userEntity is null");
        }
        if(userRepository.existsById(userEntity.getId())){ // 동일한 ID가 DB내에 존재하는 경우
            log.warn("User with id {} already exists", userEntity.getId());
            throw new IllegalArgumentException("Same user id is already exists");
        }
        String EncryptedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(EncryptedPassword);
        return userRepository.save(userEntity);
    }

    public UserEntity findByIdAndPassword(UserEntity userEntity, String password) {
        if(userEntity == null){
            throw new IllegalArgumentException("userEntity is null");
        }
        if(password == null) {
            throw new IllegalArgumentException("password is null");
        }
        String EncryptedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(EncryptedPassword);
        return userRepository.findByIdAndPassword(userEntity.getId(), password);
    }

}
