package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.FcmTokenDTO;
import com.capstone.capstone_server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmTokenController {

  private final UserService userService;
    @PostMapping("/token")
  public ResponseEntity<?> registerFcmToken(@RequestBody FcmTokenDTO request) {

    log.info("FCM 토큰 수신: userId={}", request);
    userService.updateFcm(request);

    return ResponseEntity.ok().build();
  }


  //FCM 토큰 삭제 (로그아웃 시)
  @DeleteMapping("/logout/{id}")
  public ResponseEntity<Void> removeFcmToken(@AuthenticationPrincipal CustomUserDetails details, @RequestParam String id) {

    userService.logOut(details.getUsername(),id);

    return ResponseEntity.ok().build();
  }
}
