package com.capstone.capstone_server.controller;


import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.service.HospitalService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("hospital")
public class HospitalController {

  final HospitalService hospitalService;

  public HospitalController(HospitalService hospitalService) {
    this.hospitalService = hospitalService;
  }

  /*
  모든 병원을 리턴하는 함수
   */
  @GetMapping("/getallHs")
  public ResponseEntity<?> getHospitalService() {
    List<HospitalDTO> allHospitals = hospitalService.getAllHospitals();


    // 반환
    return ResponseEntity.ok().body(allHospitals);
  }


  /*
  추가적인 병원을 추가하는 컨트롤러
   */
  @PostMapping("/addHs")
  public ResponseEntity<?> addHospital(@RequestBody HospitalDTO hospitalDTO) {
    HospitalDTO createdHospital = hospitalService.createHospital(hospitalDTO);

    return ResponseEntity.ok().body(createdHospital);
  }

  /*
  현재 병원을 업데이트 하는 컨트롤러
   */
  @PostMapping("/updateHs")
  public ResponseEntity<?> updateHospital(@RequestBody HospitalDTO hospitalDTO) {
    HospitalDTO updatedHospital = hospitalService.updateHospital(hospitalDTO);

    return ResponseEntity.ok().body(updatedHospital);
  }

  /*
  병원을 삭제하는 컨트롤러
   */
  public ResponseEntity<?> deleteHospital(@RequestBody HospitalDTO hospitalDTO) {
    hospitalService.deleteHospital(hospitalDTO);

    return ResponseEntity.ok().build();
  }


}
