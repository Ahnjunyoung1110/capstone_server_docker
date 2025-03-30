package com.capstone.capstone_server.mapper;

import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "permission.id", target = "permission")
  @Mapping(target = "token", ignore = true)
  UserDTO EntityToDTO(UserEntity userEntity);

  @Mapping(source = "hospital.id", target = "hospital")
  List<UserDTO> EntityToDTOList(List<UserDTO> userDTOList);

}
