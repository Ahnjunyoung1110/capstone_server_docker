package com.capstone.capstone_server.controller.moderator;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.SectorDTO;
import com.capstone.capstone_server.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("sector")
@PreAuthorize("hasRole('MODERATOR')")
public class sectorModeratorController {

  private final SectorService sectorService;

  public sectorModeratorController(SectorService sectorService) {
    this.sectorService = sectorService;
  }

  @Operation(
      summary = "소속 병원 Sector반환",
      description = "중간 관리자 권한으로 유저의 병원의 sector를 리턴합니다"
  )
  @GetMapping
  public ResponseEntity<List<SectorDTO>> getSectors(
      @AuthenticationPrincipal CustomUserDetails details) {
    log.info("getSectors");
    List<SectorDTO> response = sectorService.getAllSectors(details.getUsername());
    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "소속 병원 Sector 생성",
      description = "중간 관리자 권한으로 유저의 병원의 sector를 생성합니다"
          + "필수 param: hospital, name"
  )
  @PostMapping("/createSt")
  public ResponseEntity<SectorDTO> createSector(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody SectorDTO sectorDTO) {
    log.info("createSector");
    SectorDTO response = sectorService.createSector(sectorDTO, details.getUsername());
    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "소속 병원 Sector 업데이트",
      description = "중간 관리자 권한으로 유저의 병원의 sector를 업데이트 합니다"
          + "필수 param: id, hospital, name"
  )
  @PutMapping("/updateSt/{id}")
  public ResponseEntity<SectorDTO> updateSector(@AuthenticationPrincipal CustomUserDetails details,
      @RequestBody SectorDTO sectorDTO, @PathVariable Integer id) {
    log.info("updateSector");
    SectorDTO response = sectorService.updateSector(sectorDTO, id, details.getUsername());
    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "소속 병원 Sector 삭제",
      description = "중간 관리자 권한으로 유저의 병원의 sector를 삭제 합니다"
  )
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteSector(@AuthenticationPrincipal CustomUserDetails details,
      @PathVariable Integer id) {
    log.info("deleteSector");
    sectorService.deleteSector(id, details.getUsername());
    return ResponseEntity.ok().build();
  }


}
