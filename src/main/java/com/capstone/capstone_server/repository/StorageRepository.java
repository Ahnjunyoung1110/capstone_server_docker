package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.StorageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageRepository extends JpaRepository<StorageEntity, Integer> {

  @Query("Select s from StorageEntity s where  (:valid IS NULL OR s.valid = :valid) and " +
      "(:hospitalId IS NULL OR s.hospital.id = :hospitalId)")
  List<StorageEntity> serchStorage(@Param("valid") boolean valid,
      @Param("hospitalId") Integer hospitalId);
}
