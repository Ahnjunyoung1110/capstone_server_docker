package com.capstone.capstone_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class WasteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer wasteId;

  @ManyToOne
  @JoinColumn(name = "hospitalId", nullable = false)
  private HospitalEntity hospital;

  // 창고 주키, 업데이트 예정
  private String storage;

  // 폐기물 발생 장소, 업데이트 예정
  private String wasteCreatedPosition;

  // 비콘 ID 업데이트 예정
  private Integer biconId;

  // 폐기물 종류, 업데이트 예정
  private String wasteName;

  // 폐기물 상태, 업데이트 예정
  private String wasteState;

  @Builder.Default
  private Boolean valid = true;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt; // 계정 생성일

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt; // 마지막 업데이트 일

  // 최초 생성시 실행
  // createdAt 과 updatedAt을 현재시간으로 설정한다.
  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  // 업데이트시 실행
  // 업데이트된 시간을 변경한다.
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = new Date();
  }
}
