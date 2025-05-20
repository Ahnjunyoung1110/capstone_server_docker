package com.capstone.capstone_server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private String title;
    private String message;
    private LocalDateTime sendAt;
}
