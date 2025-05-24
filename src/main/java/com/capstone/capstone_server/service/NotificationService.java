package com.capstone.capstone_server.service;

import com.capstone.capstone_server.dto.NotificationResponseDTO;
import com.capstone.capstone_server.entity.DisinfectionScheduleEntity;
import com.capstone.capstone_server.entity.NotificationEntity;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.repository.NotificationRepository;
import com.capstone.capstone_server.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;

  //병원 전체 유저에게 알림 생성
  public void createNotificationForHospitalUsers(Integer hospitalId, String title, String message,
      LocalDateTime sendAt, DisinfectionScheduleEntity disinfectionScheduleEntity,
      WasteEntity wasteEntity) {
    List<UserEntity> users = userRepository.findByHospitalId(hospitalId);
    LocalDateTime now = LocalDateTime.now();

    for (UserEntity user : users) {
      if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) {
        log.warn("FCM 토큰 없음 - user: {}", user.getUuid());
        continue;
      }

      NotificationEntity.NotificationEntityBuilder builder = NotificationEntity.builder()
          .user(user)
          .title(title)
          .message(message)
          .sendAt(sendAt)
          .sent(false);
      if (disinfectionScheduleEntity != null) {
        builder.disinfectionSchedule(disinfectionScheduleEntity);
      }
      if (wasteEntity != null) {
        builder.waste(wasteEntity);
      }
      if (sendAt == null || sendAt.isBefore(now)) {
        sendFcmToUser(user, title, message);
        builder.sent(true);
        builder.receivedAt(now);
      }

      notificationRepository.save(builder.build());
    }
  }

  //1분마다 예약된 알림을 확인하여 전송
  @Scheduled(fixedRate = 60000) // 1분마다 실행
  public void processPendingNotifications() {
    LocalDateTime now = LocalDateTime.now();
    List<NotificationEntity> dueNotifications = notificationRepository.findBySentFalseAndSendAtBefore(
        now);

    for (NotificationEntity noti : dueNotifications) {
      try {

        noti.setSent(true);
        noti.setReceivedAt(now);
        NotificationEntity response = notificationRepository.save(noti);
        sendFcmToUser(response.getUser(), response.getTitle(), response.getMessage());
      } catch (Exception e) {
        log.error("예약 알림 전송 실패 - ID: {}", noti.getId(), e);
      }
    }
  }

  //유저에게 알림 전송하는 메서드
  private void sendFcmToUser(UserEntity user, String title, String message) {
    String token = user.getFcmToken();
    if (token == null || token.isEmpty()) {
      return;
    }

    try {
      Message fcmMessage = Message.builder()
          .setToken(token)
          .setNotification(Notification.builder()
              .setTitle(title)
              .setBody(message)
              .build())
          .build();

      String response = FirebaseMessaging.getInstance().send(fcmMessage);

      log.info("FCM 전송 성공: {}, userId: {}", response, user.getUuid());
    } catch (Exception e) {
      log.error("FCM 전송 실패 - userId: {}", user.getUuid(), e);
    }
  }

  // 기존 방역 알람을 삭제하는 함수
  public void deleteNotificationByDisinfection(Integer disinfectionId) {
    log.info("Delete notification by ID: {}", disinfectionId);
    notificationRepository.deleteByDisinfectionScheduleId(disinfectionId);
  }

  public List<NotificationResponseDTO> getNotificationsForUser(String uuid) {
    List<NotificationEntity> notifications = notificationRepository.findByUser_UuidAndSentTrueOrderByReceivedAtDesc(
        uuid);
    return notifications.stream()
        .map(NotificationResponseDTO::from)
        .collect(Collectors.toList());
  }

  // 기존 폐기물 알람을 삭제하는 함수
  @Transactional
  public void deleteNotificationByWaste(WasteEntity wasteEntity) {
    log.info("Delete notification by Waste: {}", wasteEntity.getId());
    notificationRepository.deleteByWaste(wasteEntity);
  }

  public List<NotificationResponseDTO> getNotification(boolean sent,
      Integer disinfectionId, String wasteId) {
    log.info("Get notification by, sent: {}", sent);
    List<NotificationEntity> entities = notificationRepository.findByEverything(sent,
        disinfectionId, wasteId);

    return NotificationResponseDTO.fromList(entities);
  }
}
