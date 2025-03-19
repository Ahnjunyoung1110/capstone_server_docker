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

  public StorageService(StorageRepository storageRepository, HospitalService hospitalService,
      StorageMapper storageMapper) {
    this.storageRepository = storageRepository;
    this.hospitalService = hospitalService;
    this.storageMapper = storageMapper;
  }


  // 검색 메서드
  public List<StorageDTO> getAllStorages(Boolean valid, Integer hospitalId) {
    log.info("getAllStorages");
    log.info("valid: {}, hospitalId: {}", valid, hospitalId);

    // valid는 null일 수 없음
    if (valid == null) {
      log.warn("valid is null");
      throw new IllegalArgumentException("valid is null");
    }
    // hospital 조건을 걸지 않은경우 전체 검색
    if (hospitalId == null) {
      log.info("hospitalId is null");
    }

    List<StorageEntity> responseEntities = storageRepository.serchStorage(valid, hospitalId);
    // dto로 변환
    return storageMapper.toStorageDTOList(responseEntities);
  }

  // create 서비스
  public StorageDTO createStorage(StorageDTO storageDTO) {
    log.info("createStorage");
    log.info("storageDTO: {}", storageDTO);

    StorageEntity storageEntity = dtoToStorageEntity(storageDTO);
    if (storageEntity == null) {
      log.warn("storageEntity is null");
      throw new IllegalArgumentException("storageEntity is null");
    }

    log.info("storageEntity: {}", storageEntity);

    StorageEntity responseEntity =  storageRepository.save(storageEntity);
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

    Optional<StorageEntity> storageEntityOptional = storageRepository.findById(
        storageEntity.getId());
    if (storageEntityOptional.isEmpty()) {
      log.warn("storageEntity not found");
      throw new IllegalArgumentException("storageEntity not found");
    }

    // 기존 내용을 업데이트
    StorageEntity storageEntityToUpdate = storageEntityOptional.get();
    storageEntityToUpdate.setStorageName(storageEntity.getStorageName());
    storageEntityToUpdate.setHospital(storageEntity.getHospital());

    StorageEntity responseentity = storageRepository.save(storageEntityToUpdate);

    return storageMapper.toStorageDTO(responseentity);
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

    // 기존 Valid 변경
    StorageEntity storageEntityToUpdate = storageEntityOptional.get();
    storageEntityToUpdate.setValid(false);
    storageRepository.save(storageEntityToUpdate);
  }

  // DTO를 Entity로 변환하는 메서드
  public StorageEntity dtoToStorageEntity(StorageDTO dto) {
    log.info("dtoToStorageEntity");
    log.info("dto: {}", dto);
    if (dto == null) {
      log.warn("dto is null");
      throw new IllegalArgumentException("dto is null");
    }
    HospitalEntity hospitalEntity = hospitalService.getHospitalById(dto.getHospitalId());
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
