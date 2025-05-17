package com.capstone.capstone_server.service;


import com.capstone.capstone_server.dto.SectorDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.SectorEntity;
import com.capstone.capstone_server.mapper.SectorMapper;
import com.capstone.capstone_server.repository.SectorRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SectorService {

  private final HospitalService hospitalService;
  private final SectorRepository sectorRepository;
  private final SectorMapper sectorMapper;

  public SectorService(HospitalService hospitalService, SectorRepository sectorRepository,
      SectorMapper sectorMapper) {
    this.hospitalService = hospitalService;
    this.sectorRepository = sectorRepository;
    this.sectorMapper = sectorMapper;
  }

  // R
  public List<SectorDTO> getAllSectors(String Uuid) {
    log.info("Get all sectors");
    HospitalEntity hospitalEntity = hospitalService.findHospitalByUuid(Uuid);
    List<SectorEntity> sectorEntity = sectorRepository.findAllByHospitalId(hospitalEntity.getId());
    log.info("Sector found {}", sectorEntity);

    return sectorMapper.entityListToDtoList(sectorEntity);
  }

  // C
  public SectorDTO createSector(SectorDTO sectorDTO, String Uuid) {
    log.info("Create sector {}", sectorDTO);

    HospitalEntity hospitalEntity = hospitalService.findHospitalByUuid(Uuid);
    if(!sectorDTO.getHospital().equals(hospitalEntity.getId())) {
      log.warn("Wrong hospital id {}", sectorDTO.getHospital());
      throw new RuntimeException("Wrong hospital id");
    }
    SectorEntity sectorEntity = SectorEntity.builder()
        .name(sectorDTO.getName())
        .hospital(hospitalEntity)
        .build();
    SectorEntity newSectorEntity = sectorRepository.save(sectorEntity);
    return sectorMapper.entityToDTo(newSectorEntity);
  }

  // U
  public SectorDTO updateSector(SectorDTO sectorDTO, Integer Id, String Uuid) {
    log.info("Update sector {}", sectorDTO);
    if (!sectorDTO.getId().equals(Id)) {
      log.warn("Sector id {} does not match id {}", Uuid, sectorDTO.getId());
      throw new IllegalArgumentException("Sector id does not match id");
    }

    SectorEntity sectorEntity = checkEqualHospital(Uuid, sectorDTO);

    sectorEntity.setName(sectorDTO.getName());
    SectorEntity updatedSectorEntity = sectorRepository.save(sectorEntity);
    return sectorMapper.entityToDTo(updatedSectorEntity);
  }

  // D
  public void deleteSector(Integer Id, String Uuid) {
    log.info("Delete sector {}", Uuid);
    SectorEntity sectorEntity = sectorRepository.findById(Id).orElse(null);
    if (sectorEntity == null) {
      log.warn("Sector id {} does not exist", Id);
      throw new IllegalArgumentException("Sector id does not exist");
    }

    HospitalEntity hospitalEntity = hospitalService.findHospitalByUuid(Uuid);
    if (!sectorEntity.getHospital().getId().equals(hospitalEntity.getId())) {
      log.warn("Hospital id mismatch");
      throw new IllegalArgumentException("Hospital id mismatch");
    }

    sectorRepository.delete(sectorEntity);
  }

  // Entity를 리턴
  public SectorEntity findSectorById(Integer Id) {
    log.info("Find sector {}", Id);
    return sectorRepository.findById(Id).orElse(null);
  }

  // 해당 dto의 병원가 유저의 병원이 일치하는지 확인하는 함수
  private SectorEntity checkEqualHospital(String Uuid, SectorDTO sectorDTO) {
    HospitalEntity hospitalEntity = hospitalService.findHospitalByUuid(Uuid);
    if (hospitalEntity == null) {
      log.warn("Hospital not found");
      throw new IllegalArgumentException("Hospital not found");
    }
    if (sectorDTO == null) {
      log.warn("Sector DTO is null");
      throw new IllegalArgumentException("Sector DTO is null");
    }
    SectorEntity sectorEntity = sectorRepository.findById(sectorDTO.getId()).orElse(null);
    if (sectorEntity == null) {
      log.warn("Sector not found");
      throw new IllegalArgumentException("Sector not found");
    }

    // 일치하는지 확인
    if (!sectorEntity.getHospital().getId().equals(hospitalEntity.getId())) {
      log.warn("Hospital id mismatch");
      throw new IllegalArgumentException("Hospital id mismatch");
    }

    return sectorEntity;


  }


}
