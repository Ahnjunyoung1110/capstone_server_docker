package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteLogDTO;
import com.capstone.capstone_server.entity.WasteLogEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface WasteLogMapper {

  @Mapping(source = "waste.id", target = "wasteId")
  @Mapping(source = "user.uuid", target = "userId")
  @Mapping(source = "status.id", target = "statusId")
  WasteLogDTO entityToDto(WasteLogEntity entity);

  List<WasteLogDTO> entityToDtoList(List<WasteLogEntity> entityList);

}