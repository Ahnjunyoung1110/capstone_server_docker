package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.NotificationResponseDTO;
import com.capstone.capstone_server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String uuid = userDetails.getUsername();
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsForUser(uuid);
        return ResponseEntity.ok(notifications);
    }
}
