package com.capstone.capstone_server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PermissionDTO {

  private Integer id;
  private String permissionName;
  private double permissionLevel;
}
