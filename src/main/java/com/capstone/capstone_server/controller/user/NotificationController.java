package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.dto.FcmLogoutDTO;
import com.capstone.capstone_server.dto.FcmTokenDTO;
import com.capstone.capstone_server.entity.FcmTokenEntity;
import com.capstone.capstone_server.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class NotificationController {

    private final FcmTokenRepository fcmTokenRepository;

    @PostMapping("/token")
    public ResponseEntity<Void> registerFcmToken(@RequestBody FcmTokenDTO request) {
        String userId = request.getUserId();
        String token = request.getToken();

        log.info("FCM 토큰 수신: userId={}, token={}", request.getUserId(), request.getToken());

        // 이미 같은 토큰이 존재하는 경우는 저장하지 않음
        if (fcmTokenRepository.findByToken(token).isEmpty()) {
            FcmTokenEntity entity = new FcmTokenEntity();
            entity.setUserId(userId);
            entity.setToken(token);
            fcmTokenRepository.save(entity);
            log.info("토큰 저장 완료");
        } else {
            log.info("이미 등록된 토큰입니다. 저장 생략");
        }

        return ResponseEntity.ok().build();
    }
    //FCM 토큰 삭제 (로그아웃 시)
    @PostMapping("/logout")
    public ResponseEntity<Void> removeFcmToken(@RequestBody FcmLogoutDTO request) {
        String userId = request.getUserId();
        String token = request.getToken();

        log.info("로그아웃 요청: userId={}, token={}", userId, token);

        fcmTokenRepository.findByToken(token).ifPresentOrElse(existing -> {
            if (existing.getUserId().equals(userId)) {
                fcmTokenRepository.delete(existing);
                log.info("FCM 토큰 삭제 완료");
            } else {
                log.warn("유저 ID 불일치하여 삭제하지 않음");
            }
        }, () -> log.warn("존재하지 않는 토큰입니다. 삭제 스킵"));

        return ResponseEntity.ok().build();
    }
}
