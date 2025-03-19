package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.WasteTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteTypeRepository extends JpaRepository<WasteTypeEntity,Integer> {

}
