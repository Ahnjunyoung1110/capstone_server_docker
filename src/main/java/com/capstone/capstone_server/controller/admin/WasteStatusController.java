package com.capstone.capstone_server.controller.admin;


import com.capstone.capstone_server.dto.WasteStatusDTO;
import com.capstone.capstone_server.service.waste.WasteStatusService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("admin/wsStatus")
@PreAuthorize("hasRole('ADMIN')")
public class WasteStatusController {

  private final WasteStatusService wasteStatusService;

  @Autowired
  public WasteStatusController(WasteStatusService wasteStatusService) {
    this.wasteStatusService = wasteStatusService;
  }


  @Operation(
      summary = "폐기물 상태 리턴 ",
      description = "어드민 권한으로 현재 DB에 저장되어있는 폐기물의 상태를 리턴합니다."
  )
  @GetMapping
  public ResponseEntity<List<WasteStatusDTO>> getWasteStatus() {
    return ResponseEntity.ok(wasteStatusService.getWasteStatus());
  }

  @Operation(
      summary = "폐기물 상태 생성 ",
      description = "어드민 권한으로 새로운 폐기물 상태를 생성합니다."
  )
  @PostMapping("/create")
  public ResponseEntity<List<WasteStatusDTO>> createWasteStatus(
      @RequestBody WasteStatusDTO wasteStatusDTO) {
    log.info("Waste status create {}", wasteStatusDTO);

    return ResponseEntity.ok(wasteStatusService.createWasteStatus(wasteStatusDTO));
  }

  @Operation(
      summary = "폐기물 상태 업데이트 ",
      description = "어드민 권한으로 기존 폐기물 상태를 업데이트 합니다."
  )
  @PutMapping("/update")
  public ResponseEntity<List<WasteStatusDTO>> updateWasteStatus(
      @RequestBody List<WasteStatusDTO> wasteStatusDTOs) {
    log.info("Waste status update {}", wasteStatusDTOs);
    return ResponseEntity.ok(wasteStatusService.updateWasteStatus(wasteStatusDTOs));
  }

  @Operation(
      summary = "폐기물 상태 삭제 ",
      description = "어드민 권한으로 기존 폐기물 상태를 삭제 합니다."
  )
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<List<WasteStatusDTO>> deleteWasteStatus(@PathVariable int id) {
    log.info("Waste status delete {}", id);
    return ResponseEntity.ok(wasteStatusService.deleteWasteStatus(id));
  }
}
