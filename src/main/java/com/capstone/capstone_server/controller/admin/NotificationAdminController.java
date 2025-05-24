package com.capstone.capstone_server.controller.admin;

import com.capstone.capstone_server.dto.NotificationRequestDTO;
import com.capstone.capstone_server.dto.NotificationResponseDTO;
import com.capstone.capstone_server.service.NotificationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notifications")
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
public class NotificationAdminController {

  private final NotificationService notificationService;

  public NotificationAdminController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping("/hospital/{hospitalId}")
  public ResponseEntity<Void> sendNotificationToHospitalUsers(
      @PathVariable Integer hospitalId,
      @RequestBody NotificationRequestDTO request
  ) {
    log.info("병원 ID {}의 유저들에게 알림 전송 요청: title='{}', message='{}'",
        hospitalId, request.getTitle(), request.getMessage());

    notificationService.createNotificationForHospitalUsers(hospitalId, request.getTitle(),
        request.getMessage(), request.getSendAt(), null, null);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<NotificationResponseDTO>> getNotificationsForHospital(
      @RequestParam(required = false) boolean sent,
      @RequestParam(required = false) Integer disinfectionId,
      @RequestParam(required = false) String wasteId
  ) {
    log.info("알람을 획득합니다.");
    List<NotificationResponseDTO> response = notificationService.getNotification(sent,
        disinfectionId, wasteId);

    return ResponseEntity.ok(response);
  }
}
