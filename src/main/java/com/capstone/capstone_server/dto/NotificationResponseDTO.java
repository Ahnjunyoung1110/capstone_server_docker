package com.capstone.capstone_server.dto;

import com.capstone.capstone_server.entity.NotificationEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

  private String title;
  private String message;
  private LocalDateTime sendAt; // 예정 전송 시간
  private LocalDateTime receivedAt; // 실제 전송된 시간

  public static NotificationResponseDTO from(NotificationEntity entity) {
    return new NotificationResponseDTO(
        entity.getTitle(),
        entity.getMessage(),
        entity.getSendAt(),
        entity.getReceivedAt()
    );
  }

    public static List<NotificationResponseDTO> fromList(List<NotificationEntity> entities) {
        return entities.stream()
            .map(NotificationResponseDTO::from)
            .collect(Collectors.toList());
    }
}
