package com.capstone.capstone_server.dto;


import com.capstone.capstone_server.entity.DisinfectionScheduleEntity.DisinfectionStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DisinfectionScheduleDTO {

  private Integer id;
  private Integer hospital;
  private Integer sector;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String description;
  private DisinfectionStatus status;
}
