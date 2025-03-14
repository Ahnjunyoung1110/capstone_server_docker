package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.repository.HospitalRepository;
import java.util.List;
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

  // 신규 병원 생성
  public HospitalEntity createHospital(HospitalEntity hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // 기존에 존재하는 병원인지  이름과 전화번호를 통해 확인
    HospitalEntity findEntity = hospitalRepository.findByHospitalNameOrHospitalCall(
        hospital.getHospitalName(), hospital.getHospitalCall()).orElse(null);
    if (findEntity != null) {
      log.info("Hospital already exists with name " + hospital.getHospitalName());
      throw new IllegalArgumentException("Hospital already exists");
    }

    // 신규 저장
    HospitalEntity responseEntity = hospitalRepository.save(hospital);

    // 변환 후 반환
    return responseEntity;
  }

  // 기존 병원 업데이트
  public HospitalEntity updateHospital(HospitalEntity hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // 기존 존재하는 병원인지 확인
    HospitalEntity findEntity = hospitalRepository.findById(hospital.getHospitalId())
        .orElse(null);
    if (findEntity == null) {
      throw new IllegalArgumentException("Such hospital not found");
    }

    // 최종 업데이트
    HospitalEntity responseEntity = hospitalRepository.save(hospital);

    // 반환
    return responseEntity;

  }

  // 기존 병원 삭제
  public HospitalEntity deleteHospital(HospitalEntity hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // 기존 존재하는 병원인지 확인
    HospitalEntity findEntity = hospitalRepository.findById(hospital.getHospitalId())
        .orElse(null);
    if (findEntity == null) {
      throw new IllegalArgumentException("Such hospital not found");
    }

    findEntity.setValid(false);
    HospitalEntity responseEntity = hospitalRepository.save(hospital);

    return responseEntity;
  }
}
