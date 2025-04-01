package com.capstone.capstone_server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WasteAllDTO {
  private String id;
  private Integer hospital;
  private Integer storage;
  private Integer beacon;
  private Integer wasteType;
  private Integer wasteStatus;
  private String description;
  private List<WasteLogDTO> logs;
}
