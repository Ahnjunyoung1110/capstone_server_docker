package com.capstone.capstone_server.service.waste;

import com.capstone.capstone_server.dto.WasteStatusDTO;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import com.capstone.capstone_server.mapper.WasteStatusMapper;
import com.capstone.capstone_server.repository.WasteStatusRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WasteStatusService {

  private final WasteStatusRepository wasteStatusRepository;
  private final WasteStatusMapper wasteStatusMapper;

  public WasteStatusService(WasteStatusRepository wasteStatusRepository,
      WasteStatusMapper wasteStatusMapper) {
    this.wasteStatusRepository = wasteStatusRepository;
    this.wasteStatusMapper = wasteStatusMapper;
  }


  // 전체 리턴
  public List<WasteStatusDTO> getWasteStatus() {

    List<WasteStatusEntity> wasteStatusEntities = wasteStatusRepository.findAllByOrderByStatusLevelAsc();
    log.info("WasteStatusEntities size: {}", wasteStatusEntities.size());

    return wasteStatusMapper.EntityToDtoList(wasteStatusEntities);
  }

  // 특정 엔티티를 검색 후 리턴
  public WasteStatusEntity getWasteStatusEntity(int id) {
    return wasteStatusRepository.findById(id).orElse(null);
  }

  // 이전 WasteStatus를 리턴
  public WasteStatusEntity transportTrue(WasteStatusEntity wasteStatusEntity, int count) {
    if(wasteStatusRepository.count() < count + wasteStatusEntity.getStatusLevel()) {
      log.warn("already final transport");
      throw new RuntimeException("already final transport");
    }
    return wasteStatusRepository.findByStatusLevel(wasteStatusEntity.getStatusLevel() + count);
  }


  // 해당 Waste가 마지막 WasteStatus인지 확인
  public boolean checkFinal(WasteStatusEntity wasteStatusEntity) {

    return wasteStatusRepository.count() == wasteStatusEntity.getStatusLevel();
  }

  // 신규 생성
  public List<WasteStatusDTO> createWasteStatus(WasteStatusDTO wasteStatusDTO) {
    if (wasteStatusDTO == null) {
      log.error("WasteStatusDTO is null");
      throw new IllegalArgumentException("WasteStatusDTO is null");
    }

    // 최초 순서를 0번째로 생성
    WasteStatusEntity statusEntity = wasteStatusMapper.DtoToEntity(wasteStatusDTO);
    statusEntity.setStatusLevel(100);
    wasteStatusRepository.save(statusEntity);

    return updateWasteStatus(getWasteStatus());
  }

  // 기존 리스트 순서 업데이트
  public List<WasteStatusDTO> updateWasteStatus(List<WasteStatusDTO> wasteStatusDTOs) {
    if (wasteStatusDTOs == null) {
      log.error("WasteStatusDTOs is null");
      throw new IllegalArgumentException("WasteStatusDTOs is null");
    }

    if (wasteStatusDTOs.size() != wasteStatusRepository.count()) {
      log.error("WasteStatusDTOs size does not match number of wasteStatusDTOs");
      throw new IllegalArgumentException(
          "WasteStatusDTOs size does not match number of wasteStatusDTOs");
    }

    List<WasteStatusEntity> wasteStatusEntities = wasteStatusMapper.DtoToEntityList(
        wasteStatusDTOs);
    Integer statuslevel = 1;
    // 순서 지정
    for (WasteStatusEntity statusEntity : wasteStatusEntities) {
      Optional<WasteStatusEntity> updateEntity = wasteStatusRepository.findById(
          statusEntity.getId());
      if (updateEntity.isEmpty()) {
        log.error("WasteStatusEntity with id {} not found", statusEntity.getId());
        throw new IllegalArgumentException(
            "WasteStatusEntity with id " + statusEntity.getId() + " not found");
      }

      WasteStatusEntity updatedStatusEntity = updateEntity.get();

      updatedStatusEntity.setStatusLevel(statuslevel);
      updatedStatusEntity.setDescription(statusEntity.getDescription());
      wasteStatusRepository.save(updatedStatusEntity);
      statuslevel++;
    }

    log.info("WasteStatusLevel complete");

    return getWasteStatus();
  }


  // 기존 삭제
  public List<WasteStatusDTO> deleteWasteStatus(Integer statusId) {
    if (statusId == null) {
      log.error("statusId is null");
      throw new IllegalArgumentException("statusId is null");
    }

    log.info("statusId: {}", statusId);
    wasteStatusRepository.deleteById(statusId);

    return updateWasteStatus(getWasteStatus());
  }


}
