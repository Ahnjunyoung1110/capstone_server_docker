package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.HospitalEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Integer> {

  Optional<HospitalEntity> findByHospitalNameOrHospitalCall(String name, String call);

  List<HospitalEntity> findByValidTrue();

  @Query("Select u.hospital from UserEntity u Where u.uuid = :uuid")
  HospitalEntity findByUuid(@Param("uuid") String uuid);


}
