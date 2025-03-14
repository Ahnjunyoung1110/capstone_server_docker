package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.WasteEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WasteRepository extends JpaRepository<WasteEntity, Integer> {

  // 모든 활성화된 폐기물을 리턴
  List<WasteEntity> findAllByValidTrue();

  // 유저가 속한 병원의 모든 폐기물을 리턴
  @Query("Select w From WasteEntity w Where w.hospital.hospitalId = " +
      "(Select u.hospital.hospitalId From UserEntity u where u.uuid = :uuid)")
  List<WasteEntity> findAllByUuid(@Param("uuid") String uuid);

  // 유저가 속한 병원의 활성화된 모든 폐기물을 리턴
  @Query("Select w From WasteEntity w Where w.valid = true and w.hospital.hospitalId = " +
      "(Select u.hospital.hospitalId From UserEntity u where u.uuid = :uuid)")
  List<WasteEntity> findAllByValidTrue(@Param("uuid") String uuid);


}
