package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.DisinfectionScheduleDTO;
import com.capstone.capstone_server.entity.DisinfectionScheduleEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisinfectionScheduleMapper {

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "sector.id", target = "sector")
  DisinfectionScheduleDTO entityToDto(DisinfectionScheduleEntity entity);

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "sector.id", target = "sector")
  List<DisinfectionScheduleDTO> entityToDto(List<DisinfectionScheduleEntity> entity);

}
