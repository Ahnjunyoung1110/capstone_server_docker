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

  private Integer wasteID;
  private String wasteName;
  private boolean wasteStatus;
}
