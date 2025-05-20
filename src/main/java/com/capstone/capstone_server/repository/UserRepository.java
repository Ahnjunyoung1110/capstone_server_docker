package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

  boolean existsByUsername(String username);

  Optional<UserEntity> findByUsername(String username);

  List<UserEntity> findAllByHospital(HospitalEntity hospital);

  List<UserEntity> findByHospitalId(Integer hospitalId);
}
