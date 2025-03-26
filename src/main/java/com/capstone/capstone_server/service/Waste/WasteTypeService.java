package com.capstone.capstone_server.service.Waste;


import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.repository.WasteTypeRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WasteTypeService {

  private final WasteTypeRepository wasteTypeRepository;

  @Autowired
  public WasteTypeService(WasteTypeRepository wasteTypeRepository) {
    this.wasteTypeRepository = wasteTypeRepository;
  }

  // 모든 폐기물 종류를 리턴하는 함수
  public List<WasteTypeEntity> GetAllWasteTypes() {
    return wasteTypeRepository.findAll();
  }

  // 특정 Entity를 리턴하는 함수
  public WasteTypeEntity GetWasteTypeById(int id) {
    return wasteTypeRepository.findById(id).orElse(null);
  }

  // 새로운 폐기물을 생성
  public WasteTypeEntity createWasteType(WasteTypeEntity wasteType) {
    if (wasteType == null) {
      throw new IllegalArgumentException("WasteType cannot be null");
    }
    if (wasteType.getTypeName() == null || wasteType.getPeriod() == null) {
      throw new IllegalArgumentException("WasteType period or Name cannot be null");
    }

    return wasteTypeRepository.save(wasteType);
  }

  // 기존의 폐기물을 수정
  public WasteTypeEntity updateWasteType(WasteTypeEntity wasteType) {
    if (wasteType == null) {
      throw new IllegalArgumentException("WasteType cannot be null");
    }
    if (wasteType.getTypeName() == null || wasteType.getPeriod() == null) {
      throw new IllegalArgumentException("WasteType period or Name cannot be null");
    }

    return wasteTypeRepository.save(wasteType);
  }

  // 기존의 폐기물을 삭제(비활성화)
  public void deleteWasteType(WasteTypeEntity wasteType) {
    if (wasteType == null) {
      throw new IllegalArgumentException("WasteType cannot be null");
    }

    // 삭제할 기존의 폐기물 종류를 가져옴
    Optional<WasteTypeEntity> wasteTypeEntity = wasteTypeRepository.findById(wasteType.getId());
    if (wasteTypeEntity.isEmpty()) {
      throw new IllegalArgumentException("WasteType not found");
    }

    // 해당 폐기물의 활성화 상태를 false로 변경
    WasteTypeEntity deleteEntity = wasteTypeEntity.get();
    deleteEntity.setValid(false);
    wasteTypeRepository.save(deleteEntity);
  }


}
