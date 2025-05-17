package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.WasteLogEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteLogRepository extends JpaRepository<WasteLogEntity, Integer> {

  List<WasteLogEntity> findAllByWasteIdOrderByStatusStatusLevelAsc(String wasteId);
}
