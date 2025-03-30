package com.capstone.capstone_server.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasteLogDTO {
  private Integer id;
  private String description;
  private Integer status;
  private String waste;
  private String user;
  private Date createdAt;
  private Date updatedAt;
}
