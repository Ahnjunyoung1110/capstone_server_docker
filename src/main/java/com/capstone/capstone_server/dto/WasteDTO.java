package com.capstone.capstone_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WasteDTO {

  private String id;
  private Integer hospitalId;
  private Integer storageId;
  private Integer beaconId;
  private Integer wasteTypeId;
  private Integer wasteStatusId;
  private String description;
}
