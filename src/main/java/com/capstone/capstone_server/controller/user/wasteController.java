package com.capstone.capstone_server.controller.user;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.WasteAllDTO;
import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.dto.WasteLogDTO;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.mapper.WasteMapper;
import com.capstone.capstone_server.service.waste.WasteLogService;
import com.capstone.capstone_server.service.waste.WasteService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

  @Operation(
      summary = "폐기물 ",
      description = "유저 권한으로 폐기물의 정보를 받아옵니다."
  )
  @GetMapping()
  public ResponseEntity<WasteDTO> getWaste(@RequestParam String wasteId) {
    log.info("getWaste wasteId: {}", wasteId);
    return ResponseEntity.ok().body(wasteService.findWasteById(wasteId));
  }

  // 모든 폐기물을 리턴하는 함수
  @Operation(
      summary = "폐기물 정보 리턴 ",
      description = "유저 권한으로 모든 폐기물의 정보를 리턴합니다."
  )
  @GetMapping("/getAllWaste")
  public ResponseEntity<List<WasteDTO>> getAllWaste() {
    log.info("getAllWaste request");
    List<WasteEntity> wasteEntities = wasteService.getAllWastes();

    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 단일 폐기물에 대한 로그를 받아오는 컨트롤러
  @Operation(
      summary = "폐기물 로그 리턴",
      description = "유저 권한으로 폐기물의 로그를 받아옵니다."
  )
  @GetMapping("/log/{id}")
  public ResponseEntity<List<WasteLogDTO>> getWasteLog(@PathVariable String id) {
    log.info("getWasteLog wasteId: {}", id);
    List<WasteLogDTO> responseDTO = wasteLogService.getWasteLog(id);

    return ResponseEntity.ok().body(responseDTO);
  }

  // 단일 폐기물에 대한 모든 로그를 받아오는 컨트롤러
  @Operation(
      summary = "단일 폐기물 전체 정보 리턴",
      description = "id를 기준으로 해당 폐기물에 대한 정보와 로그를 리턴한다."
  )

  @GetMapping("/allData/{id}")
  public ResponseEntity<WasteAllDTO> getAllData(@PathVariable String id) {
    log.info("Get All Data of {}", id);
    WasteDTO responseWasteDTO = wasteService.findWasteById(id);
    List<WasteLogDTO> responseLogDTO = wasteLogService.getWasteLog(id);
    WasteAllDTO responseDTO = WasteAllDTO.builder()
        .id(responseWasteDTO.getId())
        .beacon(responseWasteDTO.getBeaconId())
        .hospital(responseWasteDTO.getHospitalId())
        .storage(responseWasteDTO.getStorageId())
        .wasteType(responseWasteDTO.getWasteTypeId())
        .wasteStatus(responseWasteDTO.getWasteStatusId())
        .description(responseWasteDTO.getDescription())
        .logs(responseLogDTO)
        .build();

    return ResponseEntity.ok(responseDTO);
  }


  // 유저가 속한 병원의 모든 폐기물을 리턴하는 함수
  @Operation(
      summary = "폐기물 상세 검색",
      description = "유저 권한으로 폐기물을 상세검색 합니다."
  )
  @GetMapping("/getAllWasteHs")
  public ResponseEntity<List<WasteDTO>> getAllWasteById(
      @AuthenticationPrincipal CustomUserDetails details,
      @RequestParam(required = false) String wasteId, // 폐기물 id
      @RequestParam(required = false) Integer beaconId, // 비컨 id
      @RequestParam(required = false) Integer wasteTypeId, // 폐기물 종류 Id
      @RequestParam(required = false) Integer wasteStatusId, // 폐기물 상태 Id
      @RequestParam(required = false) Integer storageId, // 창고 Id
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      // 생성일 검색
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
      // 생성일 검색
  ) {
    log.info("getAllWasteByEverything request wasteId: {}, "
            + "beaconId: {}, wasteTypeId: {}, wasteStatusId: {}, storageId: {}, "
            + "Date: {}~{}",
        wasteId, beaconId, wasteTypeId,
        wasteStatusId, storageId, startDate, endDate);

    String uuid = details.getUsername();

    List<WasteEntity> wasteEntities = wasteService.getAllWasteEverything(true, uuid, wasteId,
        beaconId, wasteTypeId, wasteStatusId, storageId, startDate, endDate);
    List<WasteDTO> wasteDTOs = wasteMapper.toDTOList(wasteEntities);
    return ResponseEntity.ok().body(wasteDTOs);
  }

  // 새로운 폐기물을 생성하는 함수
  @Operation(
      summary = "폐기물 생성",
      description = "유저 권한으로 폐기물을 생성합니다."
  )
  @PostMapping("/createWaste")
  public ResponseEntity<WasteDTO> createWaste(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody WasteDTO wasteDTO) {
    log.info("createWaste request {}", wasteDTO);
    WasteDTO responseDTO = wasteService.createWaste(wasteDTO, details.getUsername());

    return ResponseEntity.ok().body(responseDTO);
  }

  // 폐기물을 업데이트하는 함수
  @Operation(
      summary = "폐기물 업데이트",
      description = "유저 권한으로 폐기물을 업데이트."
  )
  @PutMapping("updateWaste/{id}")
  public ResponseEntity<WasteDTO> updateWaste(@AuthenticationPrincipal CustomUserDetails details,
      @PathVariable String id,
      @RequestBody WasteDTO wasteDTO) {
    log.info("updateWaste request {} , {}", wasteDTO, id);

    WasteDTO responseDTO = wasteService.updateWaste(wasteDTO, id, details.getUsername());

    return ResponseEntity.ok().body(responseDTO);
  }

  // 폐기물을 다음 Status로 변경하는 함수ㅜ
  @Operation(
      summary = "다음 폐기물 상태로 변경",
      description = "DB를 검색해서 폐기물의 상태를 다음으로 변경시킨다."
          + "description을 param으로 넣을 수 있다."
  )
  @PutMapping("/toNext/{id}")
  public ResponseEntity<WasteDTO> transportStatus(
      @AuthenticationPrincipal CustomUserDetails details, @PathVariable String id,
      @RequestParam(required = false) String description) {
    log.info("transportStatus request {}", id);
    WasteDTO wasteDTO = wasteService.toNextStatus(details.getUsername(), id, description);
    return ResponseEntity.ok().body(wasteDTO);
  }

  // 폐기물의 비콘을 해제하고 비콘의 used를 false로 만드는 함수
  @Operation(
      summary = "폐기물 비콘 변경",
      description = "폐기물의 비콘을 제거하고 해당 비콘의 used 상태를 false로 만든다."
  )
  @PutMapping("/eliminateBc/{id}")
  public ResponseEntity<WasteDTO> eliminnateBeacon(@PathVariable String id) {
    log.info("eliminate beacon controller {}", id);
    WasteDTO wasteDTO = wasteService.eliminateBeacon(id);
    return ResponseEntity.ok().body(wasteDTO);
  }

  // 폐기물을 삭제(비활성화)하는 함수
  @Operation(
      summary = "폐기물 삭제",
      description = "유저 권한으로 폐기물을 삭제(비활성화) 합니다."
  )
  @DeleteMapping("/deleteWaste/{wasteId}")
  public ResponseEntity<?> deleteWaste(@PathVariable String wasteId) {
    log.info("deleteWaste request, wasteId: {}", wasteId);

    wasteService.deleteWaste(wasteId);

    return ResponseEntity.ok().build();
  }


}
