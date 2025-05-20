package com.capstone.capstone_server.dto;

import com.capstone.capstone_server.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private String title;
    private String message;
    private LocalDateTime sendAt; // 예정 전송 시간
    private LocalDateTime receivedAt; // 실제 전송된 시간
    private boolean sent;         // 전송 완료 여부

    public static NotificationResponseDTO from(NotificationEntity entity) {
        return new NotificationResponseDTO(
                entity.getTitle(),
                entity.getMessage(),
                entity.getSendAt(),
                entity.getReceivedAt(),
                entity.isSent()
        );
    }
}
