package com.capstone.capstone_server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class WasteTypeDTO {

  private Integer id;
  private String typeName;
  private Integer period;
  private boolean active;

}
