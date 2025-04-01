package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.mapper.HospitalMapper;
import com.capstone.capstone_server.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
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
  @Operation(
      summary = "모든 병원 리턴",
      description = "유저 권한으로 DB의 모든 병원을 리턴합니다."
  )
  @GetMapping()
  public ResponseEntity<List<HospitalDTO>> getHospitalService() {
    log.info("getAllHospitalService request");
    List<HospitalEntity> allHospitals = hospitalService.getAllHospitals();

    List<HospitalDTO> allHospitalsDTO = hospitalMapper.toDtoList(allHospitals);

    // 반환
    return ResponseEntity.ok().body(allHospitalsDTO);
  }

}
