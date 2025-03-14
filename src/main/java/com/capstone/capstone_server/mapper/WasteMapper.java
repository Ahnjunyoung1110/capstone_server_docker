package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.entity.WasteEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WasteMapper {

  WasteEntity toWasteEntity(WasteDTO wasteDTO);
  WasteDTO toWasteDTO(WasteEntity wasteEntity);

  List<WasteDTO> toDTOList(List<WasteEntity> wasteEntityList);

}
