package com.capstone.capstone_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    private DisinfectionScheduleEntity disinfectionSchedule;

    @ManyToOne
    private WasteEntity waste;

    private String title;
    private String message;
    private LocalDateTime sendAt; //전송 예정 시간, null이면 즉시 전송
    private boolean sent; //전송 여부
    private LocalDateTime receivedAt;//전송된 시간
}
