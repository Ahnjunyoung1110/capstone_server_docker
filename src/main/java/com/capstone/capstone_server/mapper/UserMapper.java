package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.RoleEntity;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import com.capstone.capstone_server.entity.UserEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "hospital.id", target = "hospitalId")
  @Mapping(target = "token", ignore = true)
  @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRoles")
  UserDTO EntityToDTO(UserEntity userEntity);

  @Mapping(source = "hospital.id", target = "hospitalId")
  List<UserDTO> EntityToDTOList(List<UserEntity> entityList);

  @Named("mapRoles")
  static Set<RoleType> mapRoles(Set<RoleEntity> roles) {
    return roles.stream().map(RoleEntity::getName).collect(Collectors.toSet());
  }

}
