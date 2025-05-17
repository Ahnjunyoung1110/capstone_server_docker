package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteStatusDTO;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WasteStatusMapper {


  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  WasteStatusEntity DtoToEntity(WasteStatusDTO statusDTO);


  WasteStatusDTO EntityToDto(WasteStatusEntity statusEntity);


  List<WasteStatusDTO> EntityToDtoList(List<WasteStatusEntity> statusEntities);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  List<WasteStatusEntity> DtoToEntityList(List<WasteStatusDTO> statusDTOList);
}
