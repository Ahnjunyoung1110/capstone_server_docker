package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.PasswordChanngeDTO;
import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.mapper.UserMapper;
import com.capstone.capstone_server.security.TokenProvider;
import com.capstone.capstone_server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public UserController(TokenProvider tokenProvider, UserService userService,
      UserMapper userMapper) {
    this.tokenProvider = tokenProvider;
    this.userService = userService;
    this.userMapper = userMapper;
  }

  // 회원가입 메서드
  @Operation(
      summary = "회원가입",
      description = "유저 권한으로 회원가입을 신청합니다."
          + "필수 param: username, password, "
          + "옵션 param: name, phoneNumber, email, hospital"
  )
  @PostMapping("/signup")
  public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO userDTO) {
    log.info("Sign up user request: {}", userDTO);

    // 유저 생성 후 반환
    UserDTO responseDTO = userService.createUser(userDTO);
    return ResponseEntity.ok().body(responseDTO);
  }

  // 로그인 메서드
  @Operation(
      summary = "로그인",
      description = "유저 권한으로 로그인 후 토큰을 받아갑니다."
          + "필수 param: username, password, "
  )
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

  // 비밀번호 변경 메서드
  @Operation(
      summary = "비밀번호 변경",
      description = "유저 권한으로 본인의 비밀번호를 변경합니다."
          + "필수 param: username, password"
  )
  @PostMapping("/changePw")
  public ResponseEntity<?> changePw(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody PasswordChanngeDTO passwordChanngeDTO) {
    log.info("Change info user request: {}", passwordChanngeDTO);

    userService.updatePw(passwordChanngeDTO, details.getPassword(), details.getUsername());

    return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
  }

  @Operation(
      summary = "유저 정보 변경",
      description = "유저 권한으로 본인의 유저정보를 변경합니다. 비밀번호 제외."
          + "필수 param: username, password, name, phoneNumber, email, hospital"
  )
  @PutMapping("/changeUs")
  public ResponseEntity<UserDTO> changeInfo(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody UserDTO userDTO) {
    log.info("Change info user request: {}", userDTO);

    UserDTO response = userService.updateUser(userDTO, details.getUsername());

    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "로그아웃",
      description = "로그아웃 합니다."
  )
  @PutMapping("/logOut/{uuid}")
  public ResponseEntity<?> logOut(@AuthenticationPrincipal CustomUserDetails details,
      @PathVariable String uuid) {
    log.info("Logout user request: {}", uuid);

    userService.logOut(details.getUsername(), uuid);

    return ResponseEntity.ok().build();
  }



}
