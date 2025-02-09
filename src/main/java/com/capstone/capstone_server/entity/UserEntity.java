package com.capstone.capstone_server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid; // UUID를 기본키로 사용

    @Column(nullable = false)
    private String id; // 사용자 계정 ID(고유값)

    @Column(nullable = false)
    private String password; // 비밀번호 (해싱 후 저장 필요)

    private String name; // 사용자 이름

    private String phoneNumber; // 사용자 전화번호

    private String email; // 사용자 이메일

    private String profession; // 사용자 직업(의사, 간호사 등)

    @ManyToOne
    @JoinColumn(name = "hospitalId")
    private HospitalEntity hospital;

    @ManyToOne
    @JoinColumn(name = "permissionId")
    private PermissionEntity permission;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // 계정 생성일

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // 마지막 업데이트 일

    @Builder.Default
    private Boolean active = true;


    // 최초 생성시 실행
    // createdAt 과 updatedAt을 현재시간으로 설정한다.
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
