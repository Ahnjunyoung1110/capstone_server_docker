package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.dto.WasteStatusDTO;
import com.capstone.capstone_server.service.waste.WasteStatusService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("wsStatus")
@RestController
@Slf4j
public class WasteStatusUserController {

  private final WasteStatusService wasteStatusService;

  public WasteStatusUserController(WasteStatusService wasteStatusService) {
    this.wasteStatusService = wasteStatusService;
  }

  @Operation(
      summary = "폐기물 상태 리턴",
      description = "유저 권한으로 폐기물의 상태들을 받아옵니다."
  )
  @GetMapping
  public ResponseEntity<List<WasteStatusDTO>> getWasteStatus() {
    log.info("getWasteStatus");
    List<WasteStatusDTO> response = wasteStatusService.getWasteStatus();
    return ResponseEntity.ok(response);
  }

}
