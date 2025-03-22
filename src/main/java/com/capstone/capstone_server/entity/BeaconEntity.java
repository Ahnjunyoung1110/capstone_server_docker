package com.capstone.capstone_server.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class BeaconEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String uuid;

  @Column(nullable = false, unique = true)
  private String deviceAddress;

  @Column(nullable = false)
  private Integer major;

  @Column(nullable = false)
  private Integer minor;

  private String location;

  private String label;

  @ManyToOne
  private HospitalEntity hospital;

  @Builder.Default
  private boolean isUsed = false;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @Builder.Default
  private Boolean valid = true;


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
