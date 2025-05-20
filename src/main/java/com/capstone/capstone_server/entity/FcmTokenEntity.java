package com.capstone.capstone_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fcm_tokens")
public class FcmTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime createdAt = LocalDateTime.now();


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}