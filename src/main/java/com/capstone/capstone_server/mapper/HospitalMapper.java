package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

  HospitalMapper INSTANCE = Mappers.getMapper(HospitalMapper.class);

  HospitalEntity toEntity(HospitalDTO dto);

  HospitalDTO toDto(HospitalEntity entity);

  List<HospitalDTO> toDtoList(List<HospitalEntity> entities);
}
