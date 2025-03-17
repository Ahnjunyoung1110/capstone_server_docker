package com.capstone.capstone_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid; // UUID를 기본키로 사용

  @Column(nullable = false, unique = true)
  private String username; // 사용자 계정 ID(고유값)

  @Column(nullable = false)
  private String password; // 비밀번호 (해싱 후 저장 필요)

  private String name; // 사용자 이름

  private String phoneNumber; // 사용자 전화번호

  private String email; // 사용자 이메일

  private String profession; // 사용자 직업(의사, 간호사 등)

  @ManyToOne
  @JoinColumn(name = "hospital_id")
  private HospitalEntity hospital;

  @ManyToOne
  @JoinColumn(name = "permissionId")
  private PermissionEntity permission;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt; // 계정 생성일

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt; // 마지막 업데이트 일

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<RoleEntity> roles = new HashSet<>();

  @Builder.Default
  private Boolean valid = false;


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
