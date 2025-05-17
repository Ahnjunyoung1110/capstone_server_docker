package com.capstone.capstone_server.mapper;


import com.capstone.capstone_server.dto.SectorDTO;
import com.capstone.capstone_server.entity.SectorEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SectorMapper {

  @Mapping(source = "hospital.id", target = "hospital")
  SectorDTO entityToDTo(SectorEntity entity);

  @Mapping(source = "hospital.id", target = "hospital")
  List<SectorDTO> entityListToDtoList(List<SectorEntity> entities);
}
