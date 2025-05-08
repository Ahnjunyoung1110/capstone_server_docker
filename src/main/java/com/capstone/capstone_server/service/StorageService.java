package com.capstone.capstone_server.service;


import com.capstone.capstone_server.dto.StorageDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.StorageEntity;
import com.capstone.capstone_server.mapper.StorageMapper;
import com.capstone.capstone_server.repository.StorageRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StorageService {

  private final StorageRepository storageRepository;
  private final HospitalService hospitalService;
  private final StorageMapper storageMapper;
  private final BeaconService beaconService;

  public StorageService(StorageRepository storageRepository, HospitalService hospitalService,
      StorageMapper storageMapper, BeaconService beaconService) {
    this.storageRepository = storageRepository;
    this.hospitalService = hospitalService;
    this.storageMapper = storageMapper;
    this.beaconService = beaconService;
  }


  // 검색 메서드
  public List<StorageDTO> getAllStorages(Integer hospitalId) {
    log.info("getAllStorages");
    log.info("hospitalId: {}", hospitalId);

    // hospital 조건을 걸지 않은경우 전체 검색
    if (hospitalId == null) {
      log.info("hospitalId is null");
    }

    List<StorageEntity> responseEntities = storageRepository.serchStorage(hospitalId);
    // dto로 변환
    return storageMapper.toStorageDTOList(responseEntities);
  }

  // 단일 창고 검색 메서드
  public StorageEntity getStorageById(Integer id) {
    log.info("getStorageById");
    log.info("id: {}", id);
    if (id == null) {
      log.warn("id is null");
      throw new IllegalArgumentException("id is null");
    }

    Optional<StorageEntity> responseEntity = storageRepository.findById(id);
    return responseEntity.orElse(null);
  }

  // create 서비스
  public StorageDTO createStorage(String uuid, StorageDTO storageDTO) {
    log.info("createStorage");
    log.info("storageDTO: {}", storageDTO);

    StorageEntity storageEntity = dtoToStorageEntity(storageDTO);
    if (storageEntity == null || uuid == null) {
      log.warn("storageEntity is null");
      throw new IllegalArgumentException("storageEntity is null");
    }

    log.info("storageEntity: {}", storageEntity);
    HospitalEntity hospitalEntity = hospitalService.findHospitalByUuid(uuid);

    storageEntity.setHospital(hospitalEntity);
    StorageEntity responseEntity = storageRepository.save(storageEntity);
    return storageMapper.toStorageDTO(responseEntity);
  }

  // update 서비스
  public StorageDTO updateStorage(StorageDTO storageDTO) {
    log.info("updateStorage");
    log.info("storageDTO: {}", storageDTO);

    StorageEntity storageEntity = dtoToStorageEntity(storageDTO);
    if (storageEntity == null) {
      log.warn("storageEntity is null");
      throw new IllegalArgumentException("storageEntity is null");
    }

    // id를 기준으로 기존의 storage를 가져오기
    Optional<StorageEntity> storageEntityOptional = storageRepository.findById(
        storageEntity.getId());
    if (storageEntityOptional.isEmpty()) {
      log.warn("storageEntity not found");
      throw new IllegalArgumentException("storageEntity not found");
    }

    // 기존 내용을 업데이트
    StorageEntity storageEntityToUpdate = storageEntityOptional.get();

    // 제공된 beaconId를 기반으로 비콘을 db에서 찾아와 변경하기
    if (storageDTO.getBeacon() != null) {
      storageEntity.setBeacon(beaconService.getBeaconEntityById(storageDTO.getBeacon()));
      storageEntityToUpdate.setBeacon(storageEntity.getBeacon());
    }
    if (storageDTO.getStorageName() != null) {
      storageEntityToUpdate.setStorageName(storageDTO.getStorageName());
    }
    StorageEntity response = storageRepository.save(storageEntityToUpdate);

    return storageMapper.toStorageDTO(response);
  }

  // Delete 서비스
  public void deleteStorage(Integer storageId) {
    log.info("deleteStorage");
    log.info("storageId: {}", storageId);
    if (storageId == null) {
      log.warn("storageId is null");
      throw new IllegalArgumentException("storageEntity is null");
    }

    Optional<StorageEntity> storageEntityOptional = storageRepository.findById(storageId);
    if (storageEntityOptional.isEmpty()) {
      log.warn("storageEntity not found");
      throw new IllegalArgumentException("storageEntity not found");
    }

    // 삭제
    storageRepository.delete(storageEntityOptional.get());
  }

  // DTO를 Entity로 변환하는 메서드
  public StorageEntity dtoToStorageEntity(StorageDTO dto) {
    log.info("dtoToStorageEntity");
    log.info("dto: {}", dto);
    if (dto == null) {
      log.warn("dto is null");
      throw new IllegalArgumentException("dto is null");
    }
    HospitalEntity hospitalEntity = hospitalService.getHospitalById(dto.getHospital());
    if (hospitalEntity == null) {
      log.warn("hospitalEntity is null");
      throw new IllegalArgumentException("hospitalEntity is null");
    }

    return StorageEntity.builder()
        .storageName(dto.getStorageName())
        .hospital(hospitalEntity)
        .id(dto.getId())
        .build();
  }

}
