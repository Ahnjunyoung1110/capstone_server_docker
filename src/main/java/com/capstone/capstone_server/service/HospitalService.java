package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.repository.HospitalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    // 모든 병원 객체 리턴
    public List<HospitalEntity> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    // 신규 병원 생성
    public HospitalEntity createHospital(HospitalEntity hospital) {
        if(hospital == null){
            throw new IllegalArgumentException("hospital cannot be null");
        }
        return hospitalRepository.save(hospital);
    }
}
