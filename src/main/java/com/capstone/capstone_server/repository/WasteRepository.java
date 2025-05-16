package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.WasteEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WasteRepository extends JpaRepository<WasteEntity, String> {

  // 유저가 속한 병원의 활성화된 모든 폐기물을 리턴
  @Query("Select w From WasteEntity w Where w.valid = true and w.hospital.id = " +
      "(Select u.hospital.id From UserEntity u where u.uuid = :uuid)")
  List<WasteEntity> findAllByValidTrue(@Param("uuid") String uuid);

  // 상세 검색용 쿼리
  @Query("Select w From WasteEntity w Where  (:valid IS NULL OR w.valid = :valid) " +
      "and (:wasteId IS NULL OR w.id LIKE CONCAT(:wasteId, '%')) " +
      "and w.hospital.id = (Select u.hospital.id From UserEntity u where (:uuid IS NULL OR u.uuid = :uuid)) "
      +
      "and (:beaconId IS NULL OR w.beacon.id = :beaconId) " +
      "and (:wasteTypeId IS NULL OR w.wasteType.id = :wasteTypeId) " +
      "and (:wasteStatusId IS NULL OR w.wasteStatus.id = :wasteStatusId) " +
      "and (:storageId IS NULL OR w.storage.id = :storageId) " +
      "and (:startDate IS NULL OR w.createdAt >= :startDate)" +
      "and (:endDate IS NULL OR w.createdAt <= :endDate)"
  )
  List<WasteEntity> findWasteEntityEverything(@Param("valid") boolean valid,
      @Param("wasteId") String wasteId,
      @Param("uuid") String uuid,
      @Param("beaconId") Integer beaconId,
      @Param("wasteTypeId") Integer wasteTypeId, @Param("wasteStatusId") Integer wasteStatusId,
      @Param("storageId") Integer storageId, @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);


  @Query("SELECT MAX(w.id) FROM WasteEntity w WHERE w.id LIKE :prefix")
  String findMaxIdLike(@Param("prefix") String prefix);
}
