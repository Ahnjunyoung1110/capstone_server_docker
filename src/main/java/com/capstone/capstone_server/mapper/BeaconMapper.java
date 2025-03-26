package com.capstone.capstone_server.mapper;


import com.capstone.capstone_server.dto.BeaconDTO;
import com.capstone.capstone_server.entity.BeaconEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeaconMapper {
  @Mapping(source = "hospital.id", target = "hospitalId")
  BeaconDTO toBeaconDTO(BeaconEntity entity);

  @Mapping(source = "hospital.id", target = "hospitalId")
  List<BeaconDTO> toBeaconDTOList(List<BeaconEntity> entities);

}
