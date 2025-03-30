package com.capstone.capstone_server.mapper;


import com.capstone.capstone_server.dto.PermissionDTO;
import com.capstone.capstone_server.entity.PermissionEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

  @Mapping(target = "valid", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  PermissionEntity toPermissionEntity(PermissionDTO dto);

  PermissionDTO toPermissionDTO(PermissionEntity permissionEntity);

  List<PermissionDTO> permissionEntityToPermissionDTOList(
      List<PermissionEntity> permissionEntities);

}
