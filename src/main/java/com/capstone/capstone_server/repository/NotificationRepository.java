package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // 특정 유저의 알림 목록 조회 (유저 앱에서 조회용)
    List<NotificationEntity> findByUser_UuidAndSentTrueOrderByReceivedAtDesc(String uuid);

    List<NotificationEntity> findBySentFalseAndSendAtBefore(LocalDateTime now);
}