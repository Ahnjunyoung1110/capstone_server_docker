package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    public Boolean existsById(String id);
    public UserEntity findById(String id);
}
