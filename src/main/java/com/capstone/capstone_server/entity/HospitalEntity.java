package com.capstone.capstone_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalEntity {
    @Id
    private int hospitalId;

    private String hospitalName;

    private String hospitalCall;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Builder.Default
    private Boolean isValid = true;


    // 최초 생성시 실행
    // createdAt 과 updatedAt을 현재시간으로 설정한다.
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
