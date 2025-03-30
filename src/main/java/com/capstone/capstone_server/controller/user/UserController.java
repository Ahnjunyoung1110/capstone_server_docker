package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.mapper.UserMapper;
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
  private final UserMapper userMapper;

  @Autowired
  public UserController(TokenProvider tokenProvider, UserService userService, UserMapper userMapper) {
    this.tokenProvider = tokenProvider;
    this.userService = userService;
    this.userMapper = userMapper;
  }

  // 회원가입 메서드
  @PostMapping("/signup")
  public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO userDTO) {
    log.info("Sign up user request: {}", userDTO);

    // 유저 생성 후 반환
    UserDTO responseDTO = userService.createUser(userDTO);
    return ResponseEntity.ok().body(responseDTO);
  }

  // 로그인 메서드
  @PostMapping("/signin")
  public ResponseEntity<UserDTO> signIn(@RequestBody UserDTO userDTO) {
    log.info("Sign in user request: {}", userDTO);

    // 주어진 ID와 password를 이용해서 유저를 검색
    UserEntity userEntity = userService.findByIdAndPassword(userDTO.getUsername(),
        userDTO.getPassword());

    // 토큰 생성
    String token = tokenProvider.generateToken(userEntity);

    // 리턴
    UserDTO responseDTO = userMapper.EntityToDTO(userEntity);
    responseDTO.setToken(token);

    return ResponseEntity.ok().body(responseDTO);
  }


}
