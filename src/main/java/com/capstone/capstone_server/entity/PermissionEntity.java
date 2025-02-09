package com.capstone.capstone_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionEntity {
    @Id
    private String permissionId;

    private String permissionName;

    @Column(nullable = false)
    private Integer permissionLevel;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Builder.Default
    private boolean isActive = true;


    // 최초 생성시 실행
    // createdAt 과 updatedAt을 현재시간으로 설정한다.
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
