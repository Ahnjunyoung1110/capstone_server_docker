package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WasteTypeMapper {

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  WasteTypeEntity toWasteTypeEntity(WasteTypeDTO wasteDTO);

  WasteTypeDTO toWasteDTO(WasteTypeEntity wasteTypeEntity);

  List<WasteTypeDTO> toDTOList(List<WasteTypeEntity> wasteTypeEntities);

}
