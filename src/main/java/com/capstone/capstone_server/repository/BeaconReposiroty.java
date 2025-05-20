package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.BeaconEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeaconReposiroty extends JpaRepository<BeaconEntity, Integer> {

  List<BeaconEntity> findAllByHospitalIdAndValidIsTrue(Integer hospitalId);

  BeaconEntity findByDeviceAddressAndValidIsTrue(String deviceAddress);

  List<BeaconEntity> findAllByDeviceAddressInAndValidIsTrue(List<String> deviceAddresses);
}
