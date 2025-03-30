package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.entity.WasteEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface WasteMapper {

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "storage.id", target = "storage")
  @Mapping(source = "beacon.id", target = "beacon")
  @Mapping(source = "wasteType.id", target = "wasteType")
  @Mapping(source = "wasteStatus.id", target = "wasteStatus")
  WasteDTO toWasteDTO(WasteEntity wasteEntity);

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "storage.id", target = "storage")
  @Mapping(source = "beacon.id", target = "beacon")
  @Mapping(source = "wasteType.id", target = "wasteType")
  @Mapping(source = "wasteStatus.id", target = "wasteStatus")
  List<WasteDTO> toDTOList(List<WasteEntity> wasteEntityList);

}
