package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity,String> {
}
