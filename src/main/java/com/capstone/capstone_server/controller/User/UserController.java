package com.capstone.capstone_server.controller.User;


import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.security.TokenProvider;
import com.capstone.capstone_server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Slf4j
public class UserController {

  private final UserService userService;
  private final TokenProvider tokenProvider;

  @Autowired
  public UserController(TokenProvider tokenProvider, UserService userService) {
    this.tokenProvider = tokenProvider;
    this.userService = userService;
  }

  // 회원가입 메서드
  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
    log.info("Sign up user request: {}", userDTO);
    // DTO를 엔티티로 변경
    UserEntity userEntity = UserEntity.builder()
        .username(userDTO.getUsername())
        .password(userDTO.getPassword())
        .email(userDTO.getEmail())
        .name(userDTO.getName())
        .profession(userDTO.getProfession())
        .phoneNumber(userDTO.getPhoneNumber())
        .build();

    // 유저 생성 후 반환
    UserEntity responseEntity = userService.createUser(userEntity);
    UserDTO responseDTO = UserDTO.builder().username(responseEntity.getUsername())
        .password(responseEntity.getPassword()).build();
    return ResponseEntity.ok().body(responseDTO);
  }

  // 로그인 메서드
  @PostMapping("/signin")
  public ResponseEntity<?> signIn(@RequestBody UserDTO userDTO) {
    log.info("Sign in user request: {}", userDTO);

    // 주어진 ID와 password를 이용해서 유저를 검색
    UserEntity userEntity = userService.findByIdAndPassword(userDTO.getUsername(),
        userDTO.getPassword());

    // 토큰 생성
    String token = tokenProvider.generateToken(userEntity);

    // 리턴
    UserDTO responseDTO = UserDTO.builder().token(token).build();
    return ResponseEntity.ok().body(responseDTO);
  }
}
