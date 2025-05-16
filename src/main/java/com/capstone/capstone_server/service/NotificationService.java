package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.NotificationEntity;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.NotificationRepository;
import com.capstone.capstone_server.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public void sendToHospitalUsers(Long hospitalId, String title, String message) {
        List<UserEntity> users = userRepository.findByHospitalId(hospitalId);

        for (UserEntity user : users) {
            String token = user.getFcmToken();
            if (token == null || token.isEmpty()) {
                log.warn("사용자 {} 의 FCM 토큰이 존재하지 않음", user.getUuid());
                continue;
            }

            try {
                Message fcmMessage = Message.builder()
                        .setToken(token)
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(message)
                                .build())
                        .build();

                FirebaseMessaging.getInstance().send(fcmMessage);

                // 알림 DB 저장
                NotificationEntity notification = NotificationEntity.builder()
                        .user(user)
                        .title(title)
                        .message(message)
                        .sentAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);
            } catch (Exception e) {
                log.error("FCM 전송 실패 - userId: {}, token: {}", user.getUuid(), token, e);
            }
        }
    }
}
