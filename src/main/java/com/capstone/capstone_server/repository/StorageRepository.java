package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.StorageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageRepository extends JpaRepository<StorageEntity, Integer> {

  @Query("Select s from StorageEntity s where (:hospitalId IS NULL OR s.hospital.id = :hospitalId) ")
  List<StorageEntity> serchStorage(@Param("hospitalId") Integer hospitalId);

  List<StorageEntity> findAllByHospital(HospitalEntity hospital);
}
