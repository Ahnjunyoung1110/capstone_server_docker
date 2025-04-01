package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.entity.WasteEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface WasteMapper {

  @Mapping(source = "hospital.id", target = "hospitalId")
  @Mapping(source = "storage.id", target = "storageId")
  @Mapping(source = "beacon.id", target = "beaconId")
  @Mapping(source = "wasteType.id", target = "wasteTypeId")
  @Mapping(source = "wasteStatus.id", target = "wasteStatusId")
  WasteDTO toWasteDTO(WasteEntity wasteEntity);

  @Mapping(source = "hospital.id", target = "hospitalId")
  @Mapping(source = "storage.id", target = "storageId")
  @Mapping(source = "beacon.id", target = "beaconId")
  @Mapping(source = "wasteType.id", target = "wasteTypeId")
  @Mapping(source = "wasteStatus.id", target = "wasteStatusId")
  List<WasteDTO> toDTOList(List<WasteEntity> wasteEntityList);

}
