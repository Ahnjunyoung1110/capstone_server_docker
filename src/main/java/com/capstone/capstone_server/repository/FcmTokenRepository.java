package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {
    Optional<FcmTokenEntity> findByToken(String token);
    void deleteByToken(String token);
}