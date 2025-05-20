package com.capstone.capstone_server.dto;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorDTO {

  private Integer id;
  private String name;
  private Integer hospital;
  private Date createdAt;
  private Date updatedAt;
}
