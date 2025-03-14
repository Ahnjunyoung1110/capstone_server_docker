package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.PermissionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {

  // 활성화 되어있는 권한을 Permission을 기준으로 정렬하여 리턴
  List<PermissionEntity> findAllByValidTrueOrderByPermissionLevelAsc();
}
