package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.repository.HospitalRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HospitalService {

  private final HospitalRepository hospitalRepository;

  @Autowired
  public HospitalService(HospitalRepository hospitalRepository) {
    this.hospitalRepository = hospitalRepository;
  }

  // 활성화된 모든 병원 객체 리턴
  public List<HospitalEntity> getAllHospitals() {
    return hospitalRepository.findByValidTrue();
  }

  // 특정 병원 Entity 리턴
  public HospitalEntity getHospitalById(Integer id) {
    Optional<HospitalEntity> hospital = hospitalRepository.findById(id);
    return hospital.orElse(null);

  }

  public HospitalEntity findHospitalByUuid(String uuid) {
    return hospitalRepository.findByUuid(uuid);
  }

  // 신규 병원 생성
  public HospitalEntity createHospital(HospitalEntity hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // 기존에 존재하는 병원인지  이름과 전화번호를 통해 확인
    HospitalEntity findEntity = hospitalRepository.findByHospitalNameOrHospitalCallAndValidIsTrue(
        hospital.getHospitalName(), hospital.getHospitalCall()).orElse(null);
    if (findEntity != null) {
      log.info("Hospital already exists with name {}", hospital.getHospitalName());
      throw new IllegalArgumentException("Hospital already exists");
    }

    log.info("Creating hospital {}", hospital.getHospitalName());

    // 반환
    return hospitalRepository.save(hospital);
  }

  // 기존 병원 업데이트
  public HospitalEntity updateHospital(HospitalEntity hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // 기존 존재하는 병원인지 확인
    HospitalEntity findEntity = hospitalRepository.findById(hospital.getId())
        .orElse(null);
    if (findEntity == null) {
      throw new IllegalArgumentException("Such hospital not found");
    }

    // 기존에 존재하는 병원인지  이름과 전화번호를 통해 확인
    findEntity = hospitalRepository.findByHospitalNameOrHospitalCallAndValidIsTrue(
        hospital.getHospitalName(), hospital.getHospitalCall()).orElse(null);
    if (findEntity != null) {
      log.info("Hospital already exists with name {}", hospital.getHospitalName());
      throw new IllegalArgumentException("Hospital already exists");
    }

    // 기존 생성 시간 유지
    hospital.setCreatedAt(findEntity.getCreatedAt());

    // 반환
    return hospitalRepository.save(hospital);

  }

  // 기존 병원 삭제
  public HospitalEntity deleteHospital(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // 기존 존재하는 병원인지 확인
    Optional<HospitalEntity> findEntity = hospitalRepository.findById(id);
    if (findEntity.isEmpty()) {
      throw new IllegalArgumentException("Such hospital not found");
    }
    HospitalEntity hospital = findEntity.get();
    hospital.setValid(false);

    return hospitalRepository.save(hospital);
  }
}
