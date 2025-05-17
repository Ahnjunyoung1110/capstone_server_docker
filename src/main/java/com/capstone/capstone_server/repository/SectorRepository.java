package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.SectorEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<SectorEntity,Integer> {

  List<SectorEntity> findAllByHospitalId(Integer id);
}
