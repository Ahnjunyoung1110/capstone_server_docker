package com.capstone.capstone_server.controller.admin;


import com.capstone.capstone_server.dto.HospitalDTO;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.mapper.HospitalMapper;
import com.capstone.capstone_server.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("admin/hospital")
@PreAuthorize("hasRole('ADMIN')")
public class HospitalAdminController {

  final HospitalService hospitalService;
  final HospitalMapper hospitalMapper;

  public HospitalAdminController(HospitalService hospitalService, HospitalMapper hospitalMapper) {
    this.hospitalService = hospitalService;
    this.hospitalMapper = hospitalMapper;
  }


  /*
  추가적인 병원을 추가하는 컨트롤러
   */
  @Operation(
      summary = "병원 추가",
      description = "어드민 권한으로 병원을 추가합니다."
  )
  @PostMapping("/addHs")
  public ResponseEntity<HospitalDTO> addHospital(@RequestBody HospitalDTO hospitalDTO) {
    log.info("addHospital request: {}", hospitalDTO);

    HospitalEntity hospitalEntity = hospitalMapper.toEntity(hospitalDTO);

    log.info("addHospital response: {}", hospitalEntity);

    HospitalEntity createdHospital = hospitalService.createHospital(hospitalEntity);
    HospitalDTO response = hospitalMapper.toDto(createdHospital);

    return ResponseEntity.ok().body(response);
  }

  /*
  현재 병원을 업데이트 하는 컨트롤러
   */
  @Operation(
      summary = "병원 업데이트 ",
      description = "어드민 권한으로 병원을 업데이트 합니다."
  )
  @PutMapping("/updateHs")
  public ResponseEntity<HospitalDTO> updateHospital(@RequestBody HospitalDTO hospitalDTO) {
    log.info("updateHospital request: {}", hospitalDTO.getId());

    HospitalEntity hospitalEntity = hospitalMapper.toEntity(hospitalDTO);

    HospitalEntity updatedHospital = hospitalService.updateHospital(hospitalEntity);

    HospitalDTO updatedHospitalDTO = hospitalMapper.toDto(updatedHospital);

    return ResponseEntity.ok().body(updatedHospitalDTO);
  }

  /*
  병원을 삭제하는 컨트롤러
   */
  @Operation(
      summary = "병원 삭제 ",
      description = "어드민 권한으로 병원을 삭제(비활성화) 합니다."
  )

  @DeleteMapping("/deleteHs/{id}")
  public ResponseEntity<HospitalDTO> deleteHospital(@PathVariable Integer id) {
    log.info("deleteHospital request: {}", id);

    HospitalEntity deletedEntity = hospitalService.deleteHospital(id);

    HospitalDTO deletedHospitalDTO = hospitalMapper.toDto(deletedEntity);

    return ResponseEntity.ok().body(deletedHospitalDTO);
  }


}
