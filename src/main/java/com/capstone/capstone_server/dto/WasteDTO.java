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
  private Integer hospital;
  private Integer storage;
  private Integer beacon;
  private Integer wasteType;
  private Integer wasteStatus;
  private String description;
}
