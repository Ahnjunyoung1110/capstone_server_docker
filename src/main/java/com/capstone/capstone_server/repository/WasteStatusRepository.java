package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.WasteStatusEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteStatusRepository extends JpaRepository<WasteStatusEntity, Integer> {

  List<WasteStatusEntity> findAllByOrderByStatusLevelAsc();

  WasteStatusEntity findByStatusLevel(Integer statusLevel);

}
