package com.capstone.capstone_server.mapper;


import com.capstone.capstone_server.dto.StorageDTO;
import com.capstone.capstone_server.entity.StorageEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface StorageMapper {

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "beacon.id", target = "beacon")
  StorageDTO toStorageDTO(StorageEntity entity);

  @Mapping(source = "hospital.id", target = "hospital")
  @Mapping(source = "beacon.id", target = "beacon")
  List<StorageDTO> toStorageDTOList(List<StorageEntity> entities);
}
