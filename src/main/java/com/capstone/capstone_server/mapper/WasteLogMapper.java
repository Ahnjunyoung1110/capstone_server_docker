package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.WasteLogDTO;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.entity.WasteLogEntity;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface WasteLogMapper {

  @Mapping(source = "waste.id", target = "waste")
  @Mapping(source = "user.uuid", target = "user")
  @Mapping(source = "status.id", target = "status")
  List<WasteLogDTO> entityToDtoList(List<WasteLogEntity> wasteLogEntity);


  default String map(WasteEntity waste) {
    return waste == null ? null : waste.getId();
  }

  default String map(UserEntity user) {
    return user == null ? null : user.getUuid();
  }

  default Integer map(WasteStatusEntity status) {
    return status == null ? null : status.getId();
  }
}
