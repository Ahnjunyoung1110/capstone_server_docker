package com.capstone.capstone_server.mapper;
import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;

import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WasteTypeMapper {
  WasteTypeEntity toWasteTypeEntity(WasteTypeDTO wasteDTO);
  WasteTypeDTO toWasteDTO(WasteTypeEntity wasteTypeEntity);

  List<WasteTypeDTO> toDTOList (List<WasteTypeEntity> wasteTypeEntities);

}
