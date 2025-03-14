package com.capstone.capstone_server.mapper;


import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.dto.PermissionDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

  HospitalEntity toHospitalEntity(PermissionDTO dto);

  HospitalDTO toHospitalDTO(HospitalEntity hospitalEntity);

}
