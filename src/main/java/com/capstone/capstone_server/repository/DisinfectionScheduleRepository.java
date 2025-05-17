package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.DisinfectionScheduleEntity;
import com.capstone.capstone_server.entity.HospitalEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisinfectionScheduleRepository extends
    JpaRepository<DisinfectionScheduleEntity, Integer> {

  @Query("""
        SELECT d FROM DisinfectionScheduleEntity d
        WHERE (:hospital IS NULL OR d.hospital = :hospital)
          AND (:start IS NULL OR d.startTime >= :start)
          AND (:end IS NULL OR d.endTime <= :end)
      """)
  List<DisinfectionScheduleEntity> findDisinfectionScheduleEntity(
      @Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
      @Param("hospital") HospitalEntity hospital);
}
