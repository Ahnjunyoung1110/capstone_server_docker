package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.dto.WasteLogDTO;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.mapper.WasteMapper;
import com.capstone.capstone_server.service.waste.WasteLogService;
import com.capstone.capstone_server.service.waste.WasteService;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("waste")
public class wasteController {

  private final WasteService wasteService;
  private final WasteMapper wasteMapper;
  private final WasteLogService wasteLogService;

  @Autowired
  public wasteController(WasteService wasteService, WasteMapper wasteMapper,
      WasteLogService wasteLogService) {
    this.wasteService = wasteService;
    this.wasteMapper = wasteMapper;
    this.wasteLogService = wasteLogService;
  }

  @GetMapping()
  public ResponseEntity<WasteDTO> getWaste(@RequestParam String wasteId) {
    log.info("getWaste wasteId: {}", wasteId);
    return ResponseEntity.ok().body(wasteService.findWasteById(wasteId));
  }

  // 모든 폐기물을 리턴하는 함수
  @GetMapping("/getAllWaste")
  public ResponseEntity<List<WasteDTO>> getAllWaste() {
    log.info("getAllWaste request");
    List<WasteEntity> wasteEntities = wasteService.getAllWastes();

    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 단일 폐기물에 대한 로그를 받아오는 컨트롤러
  @GetMapping("/log/{id}")
  public ResponseEntity<List<WasteLogDTO>> getWasteLog(@PathVariable String id) {
    log.info("getWasteLog wasteId: {}", id);
    List<WasteLogDTO> responseDTO = wasteLogService.getWasteLog(id);

    return ResponseEntity.ok().body(responseDTO);
  }


  // 유저가 속한 병원의 모든 폐기물을 리턴하는 함수
  @GetMapping("/getAllWasteHs")
  public ResponseEntity<List<WasteDTO>> getAllWasteById(
      @AuthenticationPrincipal CustomUserDetails details,
      @RequestParam(required = false) Boolean valid, // 활성화된 폐기물
      @RequestParam(required = false) Boolean needUser, // 유저가 속한 병원만을 리턴하는가
      @RequestParam(required = false) String wasteId, // 폐기물 id
      @RequestParam(required = false) Integer beaconId, // 비컨 id
      @RequestParam(required = false) Integer wasteTypeId, // 폐기물 종류 Id
      @RequestParam(required = false) Integer wasteStatusId, // 폐기물 상태 Id
      @RequestParam(required = false) Integer storageId, // 창고 Id
      @RequestParam(required = false) Date startDate, // 생성일 검색
      @RequestParam(required = false) Date endDate // 생성일 검색
  ) {
    log.info("getAllWasteByEverything request valid: {}, need hospital: {}, wasteId: {}, "
            + "beaconId: {}, wasteTypeId: {}, wasteStatusId: {}, storageId: {}, "
            + "Date: {}~{}",
        valid, needUser, wasteId, beaconId, wasteTypeId,
        wasteStatusId, storageId, startDate, endDate);

    String uuid = Boolean.TRUE.equals(needUser) ? details.getUsername() : null;

    List<WasteEntity> wasteEntities = wasteService.getAllWasteEverything(valid, uuid, wasteId,
        beaconId, wasteTypeId, wasteStatusId, storageId, startDate, endDate);
    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 새로운 폐기물을 생성하는 함수
  @PostMapping("/createWaste")
  public ResponseEntity<WasteDTO> createWaste(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody WasteDTO wasteDTO) {
    log.info("createWaste request {}", wasteDTO);
    WasteDTO responseDTO = wasteService.createWaste(wasteDTO, details.getUsername());

    return ResponseEntity.ok().body(responseDTO);
  }

  // 폐기물을 업데이트하는 함수
  @PutMapping("updateWaste/{id}")
  public ResponseEntity<WasteDTO> updateWaste(@AuthenticationPrincipal CustomUserDetails details,
      @PathVariable String id,
      @RequestBody WasteDTO wasteDTO) {
    log.info("updateWaste request {} , {}", wasteDTO, id);

    WasteDTO responseDTO = wasteService.updateWaste(wasteDTO, id, details.getUsername());

    return ResponseEntity.ok().body(responseDTO);
  }

  // 폐기물을 삭제(비활성화)하는 함수
  @DeleteMapping("/deleteWaste/{wasteId}")
  public ResponseEntity<?> deleteWaste(@PathVariable String wasteId) {
    log.info("deleteWaste request, wasteId: {}", wasteId);

    wasteService.deleteWaste(wasteId);

    return ResponseEntity.ok().build();
  }


}
