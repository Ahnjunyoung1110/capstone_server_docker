package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock //가짜 객체로 테스트
    private UserRepository userRepository;

    @InjectMocks //테스트할 대상 클래스
    private UserService userService;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); //Mock 초기화
    }

    @Test
    //생성에 성공 경우 테스트
    void createUser_success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUuid("testUserId");
        userEntity.setPassword("testPassword");
        String originalPassword = userEntity.getPassword();
        //Mock 설정, ID가 존재하지 않는 상태
        when(userRepository.existsById(userEntity.getUuid())).thenReturn(false);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        
        // 생성 후 테스트
        UserEntity testUser = userService.createUser(userEntity);
        assertNotNull(testUser);
        System.out.println("암호화 전 비밀번호: " + originalPassword);
        System.out.println("암호화 후 비밀번호: " + testUser.getPassword());
        assertTrue(passwordEncoder.matches(originalPassword, testUser.getPassword()));


    }

    @Test
    //UserEntity가 null이라 실패할 경우 테스트
    void createUser_fail() {
        UserEntity userEntity = null;
        Exception exception = assertThrows(IllegalArgumentException.class,() -> userService.createUser(userEntity));

        assertEquals("userEntity is null", exception.getMessage());
    }

    @Test
    // 해당 ID가 이미 존재하는 경우
    void createUser_fail2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUuid("testUserId");
        userEntity.setPassword("testPassword");
        
        // 이미 존재한다고 Mock 설정
        when(userRepository.existsById(userEntity.getUuid())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class,() -> userService.createUser(userEntity));

        assertEquals("Same user id is already exists", exception.getMessage());
    }
}