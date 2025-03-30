package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.RoleEntity;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

  Optional<RoleEntity> findByName(RoleType name);

  Set<RoleEntity> findAllByNameIn(Set<RoleType> types);

}
