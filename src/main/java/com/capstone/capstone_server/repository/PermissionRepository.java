package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
}
