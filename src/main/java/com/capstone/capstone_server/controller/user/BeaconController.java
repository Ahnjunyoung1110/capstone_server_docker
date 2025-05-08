package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.BeaconDTO;
import com.capstone.capstone_server.service.BeaconService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("beacon")
public class BeaconController {

  BeaconService beaconService;

  @Autowired
  public BeaconController(BeaconService beaconService) {
    this.beaconService = beaconService;
  }


  @Operation(
      summary = "소속 병원 다중 비콘 반환",
      description = "유저 권한으로 유저가 속한 병원의 비콘을 반환 합니다."
  )
  @GetMapping
  public ResponseEntity<List<BeaconDTO>> getAllBeacons(
      @AuthenticationPrincipal CustomUserDetails user) {
    log.info("getAllBeacons Controller");

    return ResponseEntity.ok(beaconService.getAllBeacons(user.getUsername()));
  }

  @Operation(
      summary = "소속 병원 단일 비콘 반환",
      description = "유저 권한으로 특정 id의 비콘을 반환 합니다."
  )
  @GetMapping("/{id}")
  public ResponseEntity<BeaconDTO> getBeaconById(@PathVariable("id") int id) {
    log.info("getBeaconById Controller");
    return ResponseEntity.ok(beaconService.getBeaconById(id));
  }


  @Operation(
      summary = "비콘 생성",
      description = "유저 권한으로 신규 비콘을 생성 합니다."
          + "필수 param: deviceAddress(맥주소 또는 uuid), "
          + "옵션 param: location, label, hospital"
  )
  @PostMapping("/createBc")
  public ResponseEntity<BeaconDTO> createBeacon(@RequestBody BeaconDTO beaconDTO) {
    log.info("createBeacon Controller");
    return ResponseEntity.ok(beaconService.createBeacon(beaconDTO));
  }

  @Operation(
      summary = "비콘 업데이트",
      description = "유저 권한으로 비콘을 업데이트 합니다."
          + "필수 param: id, deviceAddress"
  )
  @PutMapping("/updateBc/{id}")
  public ResponseEntity<BeaconDTO> updateBeacon(@PathVariable("id") int id,
      @RequestBody BeaconDTO beaconDTO) {
    log.info("updateBeacon Controller");
    return ResponseEntity.ok(beaconService.updateBeacon(id, beaconDTO));
  }

  @Operation(
      summary = "비콘 삭제",
      description = "유저 권한으로 비콘을 삭제(비활성화) 합니다."
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBeacon(@PathVariable("id") int id) {
    log.info("deleteBeacon Controller");
    beaconService.deleteBeacon(id);
    return ResponseEntity.ok().build();
  }
}
