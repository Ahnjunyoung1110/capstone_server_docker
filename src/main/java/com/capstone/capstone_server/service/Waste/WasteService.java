package com.capstone.capstone_server.service.Waste;

import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.entity.BeaconEntity;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.StorageEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteMapper;
import com.capstone.capstone_server.repository.WasteRepository;
import com.capstone.capstone_server.service.BeaconService;
import com.capstone.capstone_server.service.HospitalService;
import com.capstone.capstone_server.service.StorageService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WasteService {

  private final WasteRepository wasteRepository;
  private final HospitalService hospitalService;
  private final WasteStatusService wasteStatusService;
  private final WasteTypeService wasteTypeService;
  private final BeaconService beaconService;
  private final StorageService storageService;
  private final WasteMapper wasteMapper;

  @Autowired
  public WasteService(WasteRepository wasteRepository, HospitalService hospitalService,
      WasteStatusService wasteStatusService, WasteTypeService wasteTypeService,
      BeaconService beaconService, StorageService storageService, WasteMapper wasteMapper) {
    this.wasteRepository = wasteRepository;
    this.hospitalService = hospitalService;
    this.wasteStatusService = wasteStatusService;
    this.wasteTypeService = wasteTypeService;
    this.beaconService = beaconService;
    this.storageService = storageService;
    this.wasteMapper = wasteMapper;
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
  public WasteDTO createWaste(WasteDTO wasteDTO) {
    WasteEntity wasteEntity = dtoToEntity(wasteDTO);
    log.info("create waste : {}", wasteEntity);
    WasteEntity responseEntity = wasteRepository.save(wasteEntity);

    return wasteMapper.toWasteDTO(responseEntity);
  }

  // 폐기물 수정
  public WasteDTO updateWaste(WasteDTO wasteDTO, Integer id) {
    WasteEntity wasteEntity = dtoToEntity(wasteDTO);
    log.info("update waste : {}", wasteEntity);

    WasteEntity updateEntity = wasteRepository.findById(id).orElse(null);
    if (updateEntity == null) {
      log.error("Cannot update waste : {}", wasteEntity);
      throw new IllegalArgumentException("Cannot update waste");
    }

    updateEntity.setHospital(wasteEntity.getHospital());
    updateEntity.setWasteType(wasteEntity.getWasteType());
    updateEntity.setWasteStatus(wasteEntity.getWasteStatus());
    updateEntity.setBeacon(wasteEntity.getBeacon());
    updateEntity.setStorage(wasteEntity.getStorage());
    wasteRepository.save(updateEntity);
    return wasteMapper.toWasteDTO(updateEntity);
  }

// 폐기물 삭제
public void deleteWaste(Integer id){
    WasteEntity wasteEntity = wasteRepository.findById(id).orElse(null);
    if (wasteEntity == null) {
      log.error("Cannot delete waste : {}", id);
      throw new IllegalArgumentException("Cannot delete waste");
    }

    log.info("delete waste : {}", wasteEntity);
    wasteEntity.setValid(false);
    wasteRepository.save(wasteEntity);
}


// 폐기물 DTO를 Entity로 변환하는 함수
public WasteEntity dtoToEntity(WasteDTO wasteDTO) {
  if (wasteDTO == null) {
    log.error("waste DTO cannot be null");
    throw new IllegalArgumentException("waste DTO cannot be null");
  }

  // 병원 검색
  HospitalEntity hospital = hospitalService.getHospitalById(wasteDTO.getHospital());
  if (hospital == null) {
    log.error("hospital not found");
    throw new IllegalArgumentException("hospital not found");
  }

  // 저장 창고 검색
  StorageEntity storage = storageService.getStorageById(wasteDTO.getStorage());
  if (storage == null) {
    log.error("storage not found");
    throw new IllegalArgumentException("storage not found");
  }

  // 비콘 검색
  BeaconEntity beacon = beaconService.getBeaconEntityById(wasteDTO.getBeacon());
  if (beacon == null) {
    log.error("beacon not found");
    throw new IllegalArgumentException("beacon not found");
  }

  // wasteType 검색
  WasteTypeEntity wasteType = wasteTypeService.GetWasteTypeById(wasteDTO.getWasteType());
  if (wasteType == null) {
    log.error("waste type not found");
    throw new IllegalArgumentException("waste type not found");
  }

  // wasteStatus 검색
  WasteStatusEntity wasteStatus = wasteStatusService.getWasteStatusEntity(
      wasteDTO.getWasteStatus());
  if (wasteStatus == null) {
    log.error("waste status not found");
    throw new IllegalArgumentException("waste status not found");
  }

  return WasteEntity.builder()
      .id(wasteDTO.getId())
      .hospital(hospital)
      .storage(storage)
      .beacon(beacon)
      .wasteType(wasteType)
      .wasteStatus(wasteStatus)
      .build();

}
}
