package com.capstone.capstone_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeaconDTO {

  private Integer id;

  private String deviceAddress;

  private String location;

  private String label;

  private Integer hospitalId;

  private Boolean used;

}
