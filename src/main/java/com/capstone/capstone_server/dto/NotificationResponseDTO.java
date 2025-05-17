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
    private LocalDateTime sentAt;

    public static NotificationResponseDTO from(NotificationEntity entity) {
        return new NotificationResponseDTO(
                entity.getTitle(),
                entity.getMessage(),
                entity.getSentAt()
        );
    }
}
