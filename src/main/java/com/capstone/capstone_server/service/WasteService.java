package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.repository.HospitalRepository;
import com.capstone.capstone_server.repository.WasteRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WasteService {

  private final WasteRepository wasteRepository;
  private final HospitalRepository hospitalRepository;

  @Autowired
  public WasteService(WasteRepository wasteRepository, HospitalRepository hospitalRepository) {
    this.wasteRepository = wasteRepository;
    this.hospitalRepository = hospitalRepository;
  }

  // 병원에 관계 없이 DB에 등록된 모든 폐기물을 리턴
  public List<WasteEntity> getAllWastes() {
    return wasteRepository.findAll();
  }

  // 활성화에 관계없이 유저가 속한 병원의 모든 폐기물을 리턴하는 함수
  public List<WasteEntity> getAllWastesHs(String uuid) {
    if (uuid == null) {
      throw new IllegalArgumentException("uuid cannot be null");
    }

    // uuid를 통해 병원의 폐기물을 검색 후 리턴
    List<WasteEntity> wastes = wasteRepository.findAllByUuid(uuid);

    // 등록된 폐기물이 존재하지 않을경우
    if (wastes == null || wastes.isEmpty()) {
      throw new IllegalArgumentException("해당 병원에는 현재 등록된 폐기물이 존재하지 않습니다.");
    }
    return wastes;
  }

  // 활성화된 폐기물만을 리턴
  public List<WasteEntity> getAllWastesValidHs(String uuid) {
    if (uuid == null) {
      throw new IllegalArgumentException("uuid cannot be null");
    }

    // uuid를 통해 병원의 폐기물을 검색 후 리턴
    List<WasteEntity> wastes = wasteRepository.findAllByValidTrue(uuid);
    if (wastes == null || wastes.isEmpty()) {
      throw new IllegalArgumentException("해당 병원에는 현재 활성화된 폐기물이 존재하지 않습니다.");
    }
    return wastes;
  }

  // 상세 검색 기능 구현 예정
  //public List<WasteEntity> getSelectedWastes(String uuid) {}

  // 폐기물 생성
  public WasteEntity createWaste(WasteEntity waste, String uuid) {
    if (waste == null) {
      throw new IllegalArgumentException("waste cannot be null");
    }
    // uuid로 hospitalEntity 조회
    HospitalEntity hospitalEntity = hospitalRepository.findByUuid(uuid);
    if (hospitalEntity == null) {
      throw new IllegalArgumentException("Hospital entity not found");
    }

    waste.setHospital(hospitalEntity);
    log.info("Create waste {}", waste);
    return wasteRepository.save(waste);
  }

  // 폐기물 업데이트
  public WasteEntity updateWaste(WasteEntity waste, String uuid) {
    if (waste == null) {
      throw new IllegalArgumentException("waste cannot be null");
    }
    // uuid로 hospitalEntity 조회
    HospitalEntity hospitalEntity = hospitalRepository.findByUuid(uuid);
    if (hospitalEntity == null) {
      throw new IllegalArgumentException("Hospital entity not found");
    }

    waste.setHospital(hospitalEntity);

    log.info("Change waste to {}", waste);
    return wasteRepository.save(waste);
  }

  // 폐기물 삭제
  public WasteEntity deleteWaste(Integer wasteId) {
    if (wasteId == null) {
      throw new IllegalArgumentException("waste cannot be null");
    }
    log.info("Delete waste {}", wasteId);

    Optional<WasteEntity> waste = wasteRepository.findById(wasteId);

    if (waste.isEmpty()) {
      throw new IllegalArgumentException("waste not found");
    }

    WasteEntity wasteEntity = waste.get();
    wasteEntity.setValid(false);

    return wasteRepository.save(wasteEntity);
  }
}
