package com.capstone.capstone_server.controller.User;

import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.mapper.HospitalMapper;
import com.capstone.capstone_server.service.HospitalService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("hospital")
public class HospitalUserController {

  private final HospitalService hospitalService;
  private final HospitalMapper hospitalMapper;

  @Autowired
  public HospitalUserController(HospitalService hospitalService, HospitalMapper hospitalMapper) {
    this.hospitalService = hospitalService;
    this.hospitalMapper = hospitalMapper;
  }

  /*
  모든 병원을 리턴하는 함수
   */
  @GetMapping()
  public ResponseEntity<?> getHospitalService() {
    log.info("getAllHospitalService request");
    List<HospitalEntity> allHospitals = hospitalService.getAllHospitals();

    List<HospitalDTO> allHospitalsDTO = hospitalMapper.toDtoList(allHospitals);

    // 반환
    return ResponseEntity.ok().body(allHospitalsDTO);
  }

}
