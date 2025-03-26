package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteStatusDTO;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WasteStatusMapper {

  WasteStatusEntity DtoToEntity(WasteStatusDTO statusDTO);

  WasteStatusDTO EntityToDto(WasteStatusEntity statusEntity);

  List<WasteStatusDTO> EntityToDtoList(List<WasteStatusEntity> statusEntities);

  List<WasteStatusEntity> DtoToEntityList(List<WasteStatusDTO> statusDTOList);
}
