package com.capstone.capstone_server.service.Waste;


import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteTypeMapper;
import com.capstone.capstone_server.repository.WasteTypeRepository;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WasteTypeService {

  private final WasteTypeRepository wasteTypeRepository;
  private final WasteTypeMapper wasteTypeMapper;

  @Autowired
  public WasteTypeService(WasteTypeRepository wasteTypeRepository, WasteTypeMapper wasteTypeMapper) {
    this.wasteTypeRepository = wasteTypeRepository;
    this.wasteTypeMapper = wasteTypeMapper;
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
  public WasteTypeDTO createWasteType(WasteTypeDTO wasteTypeDTO) {
    if (wasteTypeDTO == null) {
      throw new IllegalArgumentException("WasteType cannot be null");
    }
    if (wasteTypeDTO.getTypeName() == null || wasteTypeDTO.getPeriod() == null) {
      throw new IllegalArgumentException("WasteType period or Name cannot be null");
    }

    WasteTypeEntity wasteTypeEntity = wasteTypeMapper.toWasteTypeEntity(wasteTypeDTO);
    WasteTypeEntity responseEntity = wasteTypeRepository.save(wasteTypeEntity);


    return wasteTypeMapper.toWasteDTO(responseEntity);
  }

  // 기존의 폐기물을 수정
  public WasteTypeDTO updateWasteType(WasteTypeDTO wasteTypeDTO, Integer id) {
    if (wasteTypeDTO == null || id == null) {
      throw new IllegalArgumentException("WasteType or Id cannot be null");
    }
    if (wasteTypeDTO.getTypeName() == null || wasteTypeDTO.getPeriod() == null) {
      throw new IllegalArgumentException("WasteType period or Name cannot be null");
    }

    Optional<WasteTypeEntity> optionalWasteTypeEntity = wasteTypeRepository.findById(id);
    if (optionalWasteTypeEntity.isEmpty()) {
      log.warn("WasteType with id {} not found", id);
      throw new IllegalArgumentException("WasteType not found");
    }

    WasteTypeEntity wasteTypeEntity = optionalWasteTypeEntity.get();
    wasteTypeEntity.setTypeName(wasteTypeDTO.getTypeName());
    wasteTypeEntity.setPeriod(wasteTypeDTO.getPeriod());
    WasteTypeEntity responseEntity = wasteTypeRepository.save(wasteTypeEntity);

    return wasteTypeMapper.toWasteDTO(responseEntity);
  }

  // 기존의 폐기물을 삭제(비활성화)
  public void deleteWasteType(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("WasteType cannot be null");
    }

    // 삭제할 기존의 폐기물 종류를 가져옴
    Optional<WasteTypeEntity> wasteTypeEntity = wasteTypeRepository.findById(id);
    if (wasteTypeEntity.isEmpty()) {
      throw new IllegalArgumentException("WasteType not found");
    }

    // 해당 폐기물의 활성화 상태를 false로 변경
    WasteTypeEntity deleteEntity = wasteTypeEntity.get();
    deleteEntity.setValid(false);
    wasteTypeRepository.save(deleteEntity);
  }


}
