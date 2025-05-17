package com.capstone.capstone_server.controller.moderator;


import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.DisinfectionScheduleDTO;
import com.capstone.capstone_server.entity.DisinfectionScheduleEntity.DisinfectionStatus;
import com.capstone.capstone_server.service.DisinfectionScheduleModeratorService;
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

  @GetMapping
  public ResponseEntity<List<DisinfectionScheduleDTO>> disinfectionSchedule(
      @AuthenticationPrincipal CustomUserDetails details, @RequestParam
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime start,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime end) {
    List<DisinfectionScheduleDTO> response = disinfectionScheduleModeratorService.getDisinfectionSchedule(
        details.getUsername(), start, end);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/createDi")
  public ResponseEntity<DisinfectionScheduleDTO> createDisinfection(
      @AuthenticationPrincipal CustomUserDetails details,
      @RequestBody DisinfectionScheduleDTO dto) {
    DisinfectionScheduleDTO response = disinfectionScheduleModeratorService.createDisinfectionSchedule(
        details.getUsername(), dto);
    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/updateDi")
  public ResponseEntity<DisinfectionScheduleDTO> updateDisinfection(
      @AuthenticationPrincipal CustomUserDetails details,
      @RequestBody DisinfectionScheduleDTO dto) {
    DisinfectionScheduleDTO response = disinfectionScheduleModeratorService.updateDisinfectionSchedule(
        details.getUsername(), dto);
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/deleteDi/{id}")
  public ResponseEntity<?> deleteDisinfection(@AuthenticationPrincipal CustomUserDetails details,@PathVariable Integer id) {
    disinfectionScheduleModeratorService.deleteDisinfectionSchedule(details.getUsername(), id);
    return ResponseEntity.ok().build();
  }

  // 다음 상태로 변경
  @PutMapping("/toNextDi/{id}")
  public ResponseEntity<DisinfectionScheduleDTO> nextDi(@PathVariable Integer id,@AuthenticationPrincipal CustomUserDetails details, @RequestBody
      DisinfectionStatus status) {
    DisinfectionScheduleDTO response = disinfectionScheduleModeratorService.nextDisinfectionSchedule(details.getUsername(), id, status);
    return ResponseEntity.ok().body(response);
  }
}
