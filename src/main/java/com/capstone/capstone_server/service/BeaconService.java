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

  // 전체 비컨 조회 메서드
  public List<BeaconDTO> getAllBeacons() {
    List<BeaconEntity> entities = beaconReposiroty.findAll();

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
    if (entity == null || entity.isUsed()) {
      log.info("Beacon cannot find or use");
      throw new IllegalArgumentException("Beacon not found");
    }

    return entity;
  }

  // 신규 비컨 추가 메서드
  public BeaconDTO createBeacon(BeaconDTO beaconDTO) {
    BeaconEntity entity = dToToEntitiy(beaconDTO);

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

    BeaconEntity getEntity = dToToEntitiy(beaconDTO);
    entity.setMajor(getEntity.getMajor());
    entity.setMinor(getEntity.getMinor());
    entity.setHospital(getEntity.getHospital());
    entity.setLabel(getEntity.getLabel());
    entity.setLocation(getEntity.getLocation());
    entity.setUsed(entity.isUsed());
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
        .major(beaconDTO.getMajor())
        .minor(beaconDTO.getMinor())
        .hospital(hospital)
        .location(beaconDTO.getLocation())
        .label(beaconDTO.getLabel())
        .isUsed(beaconDTO.getIsUsed())
        .build();
  }

}
