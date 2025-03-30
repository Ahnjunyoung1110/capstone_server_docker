package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "valid", ignore = true)
  HospitalEntity toEntity(HospitalDTO dto);

  HospitalDTO toDto(HospitalEntity entity);

  List<HospitalDTO> toDtoList(List<HospitalEntity> entities);
}
