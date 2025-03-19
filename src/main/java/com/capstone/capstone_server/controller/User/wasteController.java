package com.capstone.capstone_server.controller.User;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.mapper.WasteMapper;
import com.capstone.capstone_server.service.WasteService;
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
@RequestMapping("waste")
public class wasteController {

  private final WasteService wasteService;
  private final WasteMapper wasteMapper;

  @Autowired
  public wasteController(WasteService wasteService, WasteMapper wasteMapper) {
    this.wasteService = wasteService;
    this.wasteMapper = wasteMapper;
  }


  // 모든 폐기물을 리턴하는 함수
  @GetMapping("/getAllWaste")
  public ResponseEntity<?> getAllWaste() {
    log.info("getAllWaste request");
    List<WasteEntity> wasteEntities = wasteService.getAllWastes();

    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 유저가 속한 병원의 모든 폐기물을 리턴하는 함수
  @GetMapping("/getAllWasteHs")
  public ResponseEntity<?> getAllWasteById(@AuthenticationPrincipal CustomUserDetails details) {
    log.info("getAllWasteById request");
    String uuid = details.getUsername();
    List<WasteEntity> wasteEntities = wasteService.getAllWastesHs(uuid);

    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 유저가 속한 병원의 활성화된 폐기물을 리턴하는 함수
  @GetMapping("/getAllWasteValidHs")
  public ResponseEntity<?> getWasteById(@AuthenticationPrincipal CustomUserDetails details) {
    log.info("getWasteById request");
    String uuid = details.getUsername();
    List<WasteEntity> wasteEntities = wasteService.getAllWastesValidHs(uuid);

    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 새로운 폐기물을 생성하는 함수
  @PostMapping("/createWaste")
  public ResponseEntity<?> createWaste(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody WasteDTO wasteDTO) {
    log.info("createWaste request");
    WasteEntity createEntity = wasteMapper.toWasteEntity(wasteDTO);

    String uuid = details.getUsername();
    WasteEntity wasteEntity = wasteService.createWaste(createEntity, uuid);

    WasteDTO wastedto = wasteMapper.toWasteDTO(wasteEntity);

    return ResponseEntity.ok().body(wastedto);
  }

  // 폐기물을 업데이트하는 함수
  @PutMapping("updateWaste")
  public ResponseEntity<?> updateWaste(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody WasteDTO wasteDTO) {
    log.info("updateWaste request");
    WasteEntity updateEntity = wasteMapper.toWasteEntity(wasteDTO);

    String uuid = details.getUsername();
    WasteEntity wasteEntity = wasteService.updateWaste(updateEntity, uuid);

    WasteDTO wastedto = wasteMapper.toWasteDTO(wasteEntity);

    return ResponseEntity.ok().body(wastedto);
  }

  // 폐기물을 삭제(비활성화)하는 함수
  @DeleteMapping("/deleteWaste/{wasteId}")
  public ResponseEntity<?> deleteWaste(@PathVariable Integer wasteId) {
    log.info("deleteWaste request, wasteId: {}", wasteId);

    WasteEntity wasteEntity = wasteService.deleteWaste(wasteId);
    WasteDTO wastedto = wasteMapper.toWasteDTO(wasteEntity);

    return ResponseEntity.ok().body(wastedto);
  }


}
