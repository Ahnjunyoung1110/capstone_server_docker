package com.capstone.capstone_server.service;

import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.mapper.HospitalMapper;
import com.capstone.capstone_server.repository.HospitalRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final HospitalMapper hospitalMapper;

  @Autowired
  public HospitalService(HospitalRepository hospitalRepository, HospitalMapper hospitalMapper) {
    this.hospitalRepository = hospitalRepository;
    this.hospitalMapper = hospitalMapper;
  }

  // 활성화된 모든 병원 객체 리턴
  public List<HospitalDTO> getAllHospitals() {
    return hospitalMapper.toDtoList(hospitalRepository.findByValidTrue());
  }

  // 신규 병원 생성
  public HospitalDTO createHospital(HospitalDTO hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    HospitalEntity requestEntity = hospitalMapper.toEntity(hospital);

    // 기존에 존재하는 병원인지  이름과 전화번호를 통해 확인
    HospitalEntity findEntity = hospitalRepository.findByHospitalNameOrHospitalCall(
        requestEntity.getHospitalName(), requestEntity.getHospitalCall()).orElse(null);
    if (findEntity != null) {
      throw new IllegalArgumentException("Hospital already exists");
    }

    // 신규 저장
    HospitalEntity responseEntity = hospitalRepository.save(requestEntity);

    // 변환 후 반환
    return hospitalMapper.toDto(responseEntity);
  }

  // 기존 병원 업데이트
  public HospitalDTO updateHospital(HospitalDTO hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    // DTO 변환
    HospitalEntity requestEntity = hospitalMapper.toEntity(hospital);

    // 기존 존재하는 병원인지 확인
    HospitalEntity findEntity = hospitalRepository.findById(requestEntity.getHospitalId())
        .orElse(null);
    if (findEntity == null) {
      throw new IllegalArgumentException("Such hospital not found");
    }

    // 최종 업데이트
    HospitalEntity responseEntity = hospitalRepository.save(requestEntity);

    // 반환
    return hospitalMapper.toDto(responseEntity);

  }

  // 기존 병원 삭제
  public HospitalDTO deleteHospital(HospitalDTO hospital) {
    if (hospital == null) {
      throw new IllegalArgumentException("hospital cannot be null");
    }

    HospitalEntity requestEntity = hospitalMapper.toEntity(hospital);

    // 기존 존재하는 병원인지 확인
    HospitalEntity findEntity = hospitalRepository.findById(requestEntity.getHospitalId())
        .orElse(null);
    if (findEntity == null) {
      throw new IllegalArgumentException("Such hospital not found");
    }

    findEntity.setValid(false);
    HospitalEntity responseEntity = hospitalRepository.save(requestEntity);

    return hospitalMapper.toDto(responseEntity);
  }
}
