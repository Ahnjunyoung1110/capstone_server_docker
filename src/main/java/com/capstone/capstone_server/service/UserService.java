package com.capstone.capstone_server.service;


import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserEntity findByIdAndPassword(String id, String password) {
        if(id == null){
            throw new IllegalArgumentException("userEntity is null");
        }
        if(password == null) {
            throw new IllegalArgumentException("password is null");
        }
        UserEntity userEntity = userRepository.findById(id);
        
        // 등록된 아이디가 존재하지 않는 경우
        if(userEntity == null) {
            throw new IllegalArgumentException("Id is wrong");
        }
        // 패스워드가 일치하지 않는 경우
        if(!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new IllegalArgumentException("Password is wrong");
        
        return userEntity;
    }

}
