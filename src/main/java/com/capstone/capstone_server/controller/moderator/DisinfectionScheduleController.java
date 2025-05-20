package com.capstone.capstone_server.controller.moderator;


import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.DisinfectionScheduleDTO;
import com.capstone.capstone_server.entity.DisinfectionScheduleEntity.DisinfectionStatus;
import com.capstone.capstone_server.service.DisinfectionScheduleModeratorService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("disinfection")
@PreAuthorize("hasRole('MODERATOR')")
public class DisinfectionScheduleController {

  private final DisinfectionScheduleModeratorService disinfectionScheduleModeratorService;

  public DisinfectionScheduleController(
      DisinfectionScheduleModeratorService disinfectionScheduleModeratorService) {
    this.disinfectionScheduleModeratorService = disinfectionScheduleModeratorService;
  }

  @Operation(
      summary = "방역 구역 검색",
      description = "중간 관리자 권한으로 방역구역을 검색 합니다."
          + "옵션 param: start, end"
  )
  @GetMapping
  public ResponseEntity<List<DisinfectionScheduleDTO>> disinfectionSchedule(
      @AuthenticationPrincipal CustomUserDetails details, @RequestParam(required = false)
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime start,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime end) {
    List<DisinfectionScheduleDTO> response = disinfectionScheduleModeratorService.getDisinfectionSchedule(
        details.getUsername(), start, end);
    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "방역 구역 생성",
      description = "중간 관리자 권한으로 방역구역을 생성 합니다."
          + "필수 param: hospital, sector, startTime, endTime"
  )
  @PostMapping("/createDi")
  public ResponseEntity<DisinfectionScheduleDTO> createDisinfection(
      @AuthenticationPrincipal CustomUserDetails details,
      @RequestBody DisinfectionScheduleDTO dto) {
    DisinfectionScheduleDTO response = disinfectionScheduleModeratorService.createDisinfectionSchedule(
        details.getUsername(), dto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "방역 구역 업데이트",
      description = "중간 관리자 권한으로 방역구역을 업데이트 합니다."
          + "필수 param: id, hospital, sector, startTime, endTime, status"
  )
  @PutMapping("/updateDi")
  public ResponseEntity<DisinfectionScheduleDTO> updateDisinfection(
      @AuthenticationPrincipal CustomUserDetails details,
      @RequestBody DisinfectionScheduleDTO dto) {
    log.info("updateDisinfection {}", dto);
    DisinfectionScheduleDTO response = disinfectionScheduleModeratorService.updateDisinfectionSchedule(
        details.getUsername(), dto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "방역 구역 삭제",
      description = "중간 관리자 권한으로 방역구역을 삭제 합니다."
  )
  @DeleteMapping("/deleteDi/{id}")
  public ResponseEntity<?> deleteDisinfection(@AuthenticationPrincipal CustomUserDetails details,
      @PathVariable Integer id) {
    disinfectionScheduleModeratorService.deleteDisinfectionSchedule(details.getUsername(), id);
    return ResponseEntity.ok().build();
  }
  @Operation(
      summary = "방역 구역 상태 변화",
      description = "중간 관리자 권한으로 방역구역을 다음 상태로 전환 합니다."
          + "    SCHEDULED,     // 예정\n"
          + "    IN_PROGRESS,   // 진행중\n"
          + "    COMPLETED,     // 완료\n"
          + "    FAILED         // 미완료"
      + " 반드시 다음 상태로만 전환을 받아들입니다."
  )
  // 다음 상태로 변경
  @PutMapping("/toNextDi/{id}")
  public ResponseEntity<DisinfectionScheduleDTO> nextDi(@PathVariable Integer id,
      @AuthenticationPrincipal CustomUserDetails details, @RequestBody
      DisinfectionStatus status) {
    DisinfectionScheduleDTO response = disinfectionScheduleModeratorService.nextDisinfectionSchedule(
        details.getUsername(), id, status);
    return ResponseEntity.ok().body(response);
  }
}
