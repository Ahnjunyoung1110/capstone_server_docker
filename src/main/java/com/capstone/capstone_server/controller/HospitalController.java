package com.capstone.capstone_server.controller;


import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.mapper.HospitalMapper;
import com.capstone.capstone_server.service.HospitalService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("hospital")
public class HospitalController {

  final HospitalService hospitalService;
  final HospitalMapper hospitalMapper;

  public HospitalController(HospitalService hospitalService, HospitalMapper hospitalMapper) {
    this.hospitalService = hospitalService;
    this.hospitalMapper = hospitalMapper;
  }

  /*
  모든 병원을 리턴하는 함수
   */
  @GetMapping("/getallHs")
  public ResponseEntity<?> getHospitalService() {
    log.info("getAllHospitalService request");
    List<HospitalEntity> allHospitals = hospitalService.getAllHospitals();

    List<HospitalDTO> allHospitalsDTO = hospitalMapper.toDtoList(allHospitals);

    // 반환
    return ResponseEntity.ok().body(allHospitalsDTO);
  }


  /*
  추가적인 병원을 추가하는 컨트롤러
   */
  @PostMapping("/addHs")
  public ResponseEntity<?> addHospital(@RequestBody HospitalDTO hospitalDTO) {
    log.info("addHospital request: {}", hospitalDTO);

    HospitalEntity hospitalEntity = hospitalMapper.toEntity(hospitalDTO);

    log.info("addHospital response: {}", hospitalEntity);

    HospitalEntity createdHospital = hospitalService.createHospital(hospitalEntity);

    return ResponseEntity.ok().body(createdHospital);
  }

  /*
  현재 병원을 업데이트 하는 컨트롤러
   */
  @PutMapping("/updateHs")
  public ResponseEntity<?> updateHospital(@RequestBody HospitalDTO hospitalDTO) {
    log.info("updateHospital request: {}", hospitalDTO.getHospitalId());

    HospitalEntity hospitalEntity = hospitalMapper.toEntity(hospitalDTO);

    HospitalEntity updatedHospital = hospitalService.updateHospital(hospitalEntity);

    HospitalDTO updatedHospitalDTO = hospitalMapper.toDto(updatedHospital);

    return ResponseEntity.ok().body(updatedHospital);
  }

  /*
  병원을 삭제하는 컨트롤러
   */
  @DeleteMapping("/deleteHs")
  public ResponseEntity<?> deleteHospital(@RequestBody HospitalDTO hospitalDTO) {
    log.info("deleteHospital request: {}", hospitalDTO.getHospitalId());

    HospitalEntity hospitalEntity = hospitalMapper.toEntity(hospitalDTO);

    HospitalEntity deletedEntity = hospitalService.deleteHospital(hospitalEntity);

    HospitalDTO deletedHospitalDTO = hospitalMapper.toDto(deletedEntity);

    return ResponseEntity.ok().body(deletedHospitalDTO);
  }


}
