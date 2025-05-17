package com.capstone.capstone_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisinfectionScheduleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne
  private SectorEntity sector;

  @ManyToOne
  private HospitalEntity hospital;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  private String description;


  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt; // 계정 생성일

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt; // 마지막 업데이트 일

  @Enumerated(EnumType.STRING)
  private DisinfectionStatus status;


  public enum DisinfectionStatus {
    SCHEDULED,     // 예정
    IN_PROGRESS,   // 진행중
    COMPLETED,     // 완료
    FAILED         // 미완료
  }

  // 최초 생성시 실행
  // createdAt 과 updatedAt을 현재시간으로 설정한다.
  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.status = DisinfectionStatus.SCHEDULED;
  }

  // 업데이트시 실행
  // 업데이트된 시간을 변경한다.
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = new Date();
  }

}
