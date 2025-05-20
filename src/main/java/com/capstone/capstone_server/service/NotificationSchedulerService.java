package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.NotificationEntity;
import com.capstone.capstone_server.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSchedulerService {
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void processScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();

        List<NotificationEntity> dueNotifications = notificationRepository.findBySentFalseAndSendAtBefore(now);

        for (NotificationEntity noti : dueNotifications) {
            try {
                String token = noti.getUser().getFcmToken();
                fcmService.send(token, noti.getTitle(), noti.getMessage());

                noti.setSent(true);
                noti.setReceivedAt(LocalDateTime.now());
                notificationRepository.save(noti);
            } catch (Exception e) {
                log.error("예약 알림 전송 실패 - ID: {}", noti.getId(), e);
            }
        }
    }
}
