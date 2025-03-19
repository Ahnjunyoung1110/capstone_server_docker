package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.RoleEntity;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
  public Optional<RoleEntity> findByName(RoleType name);

}
