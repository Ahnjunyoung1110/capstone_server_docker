package com.capstone.capstone_server.service;

import com.capstone.capstone_server.dto.BeaconDTO;
import com.capstone.capstone_server.entity.BeaconEntity;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.mapper.BeaconMapper;
import com.capstone.capstone_server.repository.BeaconReposiroty;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BeaconService {

  private final HospitalService hospitalService;
  private final BeaconMapper beaconMapper;
  private final BeaconReposiroty beaconReposiroty;

  @Autowired
  public BeaconService(BeaconReposiroty beaconReposiroty, BeaconMapper beaconMapper,
      HospitalService hospitalService) {
    this.beaconReposiroty = beaconReposiroty;
    this.beaconMapper = beaconMapper;
    this.hospitalService = hospitalService;
  }

  // 소속 병원 전체 비컨 조회 메서드
  public List<BeaconDTO> getAllBeacons(String uuid) {
    HospitalEntity findhospital = hospitalService.findHospitalByUuid(uuid);
    List<BeaconEntity> entities = beaconReposiroty.findAllByHospitalIdAndValidIsTrue(
        findhospital.getId());

    return beaconMapper.toBeaconDTOList(entities);
  }

  // 단일 비컨
  public BeaconDTO getBeaconById(int id) {
    BeaconEntity entity = beaconReposiroty.findById(id).orElse(null);
    if (entity == null) {
      log.info("Beacon not found");
      throw new IllegalArgumentException("Beacon not found");
    }

    return beaconMapper.toBeaconDTO(entity);
  }

  // 위 함수와 동일하나 Entity로 리턴
  public BeaconEntity getBeaconEntityById(int id) {
    BeaconEntity entity = beaconReposiroty.findById(id).orElse(null);
    if (entity == null) {
      log.info("Beacon cannot find or use");
      throw new IllegalArgumentException("Beacon not found");
    }

    return entity;
  }

  // Beacon의 Mac주소로 BeaconEntity를 리턴
  public List<BeaconEntity> getAllBeaconsByMacAddress(List<String> macAddresses) {
    return beaconReposiroty.findAllByDeviceAddressInAndValidIsTrue(macAddresses);

  }

  // 신규 비컨 추가 메서드
  public BeaconDTO createBeacon(String Uuid, BeaconDTO beaconDTO) {
    BeaconEntity entity = dToToEntitiy(beaconDTO);
    entity.setUsed(false);
    HospitalEntity hospital = hospitalService.findHospitalByUuid(Uuid);
    if (!beaconDTO.getHospitalId().equals(hospital.getId())) {
      log.warn("Hospital id mismatch");
      throw new IllegalArgumentException("Hospital id mismatch");
    }

    BeaconEntity findEntity = beaconReposiroty.findByDeviceAddressAndValidIsTrue(
        entity.getDeviceAddress());
    if (findEntity != null) {
      log.warn("Beacon is already used");
      throw new IllegalArgumentException("Beacon is already used");
    }
    BeaconEntity createdEntity = beaconReposiroty.save(entity);
    log.info("Beacon created {}", createdEntity);

    return beaconMapper.toBeaconDTO(createdEntity);
  }


  // 기존 비컨 업데이트 메서드
  public BeaconDTO updateBeacon(int id, BeaconDTO beaconDTO) {
    if (beaconDTO == null || beaconDTO.getId() != id) {
      log.info("Arguments wrong");
      throw new IllegalArgumentException("Arguments wrong");
    }

    BeaconEntity entity = beaconReposiroty.findById(id).orElse(null);
    if (entity == null) {
      log.info("Beacon not found");
      throw new IllegalArgumentException("Beacon not found");
    }
    BeaconEntity findEntity = beaconReposiroty.findByDeviceAddressAndValidIsTrue(
        entity.getDeviceAddress());
    if (findEntity != null && findEntity.getId() != id) {
      log.warn("같은 맥주소가 존재합니다.");
      throw new IllegalArgumentException("같은 맥주소가 존재합니다.");
    }

    BeaconEntity getEntity = dToToEntitiy(beaconDTO);
    entity.setDeviceAddress(beaconDTO.getDeviceAddress());
    entity.setHospital(getEntity.getHospital());
    entity.setLabel(getEntity.getLabel());
    entity.setLocation(getEntity.getLocation());
    BeaconEntity updatedEntity = beaconReposiroty.save(entity);

    return beaconMapper.toBeaconDTO(updatedEntity);

  }

  // 기본 비컨 비활성화
  public void deleteBeacon(int id) {
    BeaconEntity entity = beaconReposiroty.findById(id).orElse(null);
    if (entity == null) {
      log.info("Beacon not found");
      throw new IllegalArgumentException("Beacon not found");
    }

    entity.setValid(false);
    beaconReposiroty.save(entity);

  }


  // DTO를 Entity로 변환하는 메서드
  public BeaconEntity dToToEntitiy(BeaconDTO beaconDTO) {
    HospitalEntity hospital = hospitalService.getHospitalById(beaconDTO.getHospitalId());
    if (hospital == null) {
      log.error("Hospital not found");
      throw new RuntimeException("Hospital not found");
    }

    // 변환 후 반환
    return BeaconEntity.builder()
        .id(beaconDTO.getId())
        .deviceAddress(beaconDTO.getDeviceAddress())
        .hospital(hospital)
        .location(beaconDTO.getLocation())
        .label(beaconDTO.getLabel())
        .used(beaconDTO.getUsed() != null ? beaconDTO.getUsed() : false)
        .build();
  }

}
