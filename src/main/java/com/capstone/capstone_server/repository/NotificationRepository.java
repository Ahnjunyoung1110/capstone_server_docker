package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.NotificationEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

  // 특정 유저의 알림 목록 조회 (유저 앱에서 조회용)
  List<NotificationEntity> findByUser_UuidAndSentTrueOrderByReceivedAtDesc(String uuid);

  List<NotificationEntity> findBySentFalseAndSendAtBefore(LocalDateTime now);


  void deleteByDisinfectionScheduleId(Integer disinfectionId);

  void deleteByWaste(WasteEntity wasteEntity);


  @Query(
      "Select n From NotificationEntity n Where  (:sent IS NULL OR n.sent = :sent)" +
          "and (:disinfectionId IS NULL OR n.disinfectionSchedule.id = :disinfectionId)" +
          "and (:wasteId IS NULL OR n.waste.id = :wasteId)"
  )
  List<NotificationEntity> findByEverything(
      @Param("sent") Boolean sent, @Param("disinfectionId") Integer disinfectionId,
      @Param("wasteId") String wasteId);
}