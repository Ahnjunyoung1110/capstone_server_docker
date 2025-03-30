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
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class WasteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "waste-id-generator")
  @GenericGenerator(name = "waste-id-generator", strategy = "com.capstone.capstone_server.service.waste.WasteIdGenerator")
  private String id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private HospitalEntity hospital;

  // 창고
  @ManyToOne
  @JoinColumn(nullable = false)
  private StorageEntity storage;

  // 폐기물 발생 장소, 업데이트 예정
  private String wasteCreatedPosition;

  // 비콘 ID
  @ManyToOne
  @JoinColumn(nullable = false)
  private BeaconEntity beacon;


  // 폐기물 설명
  private String description;

  // 폐기물 종류
  @ManyToOne
  @JoinColumn(nullable = false)
  private WasteTypeEntity wasteType;

  // 폐기물 상태
  @ManyToOne
  private WasteStatusEntity wasteStatus;

  private Boolean valid;

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
    this.valid = true;
  }

  // 업데이트시 실행
  // 업데이트된 시간을 변경한다.
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = new Date();
  }
}
