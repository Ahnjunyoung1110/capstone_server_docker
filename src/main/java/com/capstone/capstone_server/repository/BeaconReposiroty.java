package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.BeaconEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeaconReposiroty extends JpaRepository<BeaconEntity,Integer> {

}
