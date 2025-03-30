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

  private String uuid;

  private String deviceAddress;

  private Integer major;

  private Integer minor;

  private String location;

  private String label;

  private Integer hospitalId;

  private Boolean used;

}
