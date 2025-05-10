package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.dto.FcmTokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class NotificationController {

    @PostMapping("/token")
    public ResponseEntity<Void> registerFcmToken(@RequestBody FcmTokenDTO request) {
        String userId = request.getUserId();
        String token = request.getToken();

        log.info("FCM 토큰 수신: userId={}, token={}", request.getUserId(), request.getToken());

        // TODO: DB에 저장 또는 업데이트 로직 작성
        // 예: fcmTokenService.save(userId, token);

        return ResponseEntity.ok().build();
    }
}
