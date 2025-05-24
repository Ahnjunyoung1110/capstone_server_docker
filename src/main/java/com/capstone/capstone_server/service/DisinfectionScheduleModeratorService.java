package com.capstone.capstone_server.service;

import com.capstone.capstone_server.dto.DisinfectionScheduleDTO;
import com.capstone.capstone_server.entity.DisinfectionScheduleEntity;
import com.capstone.capstone_server.entity.DisinfectionScheduleEntity.DisinfectionStatus;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.SectorEntity;
import com.capstone.capstone_server.mapper.DisinfectionScheduleMapper;
import com.capstone.capstone_server.repository.DisinfectionScheduleRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DisinfectionScheduleModeratorService {


  private final DisinfectionScheduleRepository disinfectionScheduleRepository;
  private final HospitalService hospitalService;
  private final DisinfectionScheduleMapper disinfectionScheduleMapper;
  private final SectorService sectorService;
  private final NotificationService notificationService;

  @Autowired
  public DisinfectionScheduleModeratorService(
      DisinfectionScheduleRepository disinfectionScheduleRepository,
      HospitalService hospitalService, DisinfectionScheduleMapper disinfectionScheduleMapper,
      SectorService sectorService, NotificationService notificationService) {
    this.disinfectionScheduleRepository = disinfectionScheduleRepository;
    this.hospitalService = hospitalService;
    this.disinfectionScheduleMapper = disinfectionScheduleMapper;
    this.sectorService = sectorService;
    this.notificationService = notificationService;
  }

  public List<DisinfectionScheduleDTO> getDisinfectionSchedule(String uuid, LocalDateTime start,
      LocalDateTime end) {
    log.info("getDisinfectionSchedule");
    HospitalEntity hospital = hospitalService.findHospitalByUuid(uuid);
    List<DisinfectionScheduleEntity> entity = disinfectionScheduleRepository.findDisinfectionScheduleEntity(
        start, end, hospital);

    return disinfectionScheduleMapper.entityToDto(entity);
  }

  // C
  public DisinfectionScheduleDTO createDisinfectionSchedule(String uuid,
      DisinfectionScheduleDTO disinfectionScheduleDTO) {
    log.info("createDisinfectionSchedule {}", disinfectionScheduleDTO);
    DisinfectionScheduleEntity disinfectionScheduleEntity = checkCollectId(uuid,
        disinfectionScheduleDTO);
    disinfectionScheduleEntity.setStatus(DisinfectionStatus.SCHEDULED);
    DisinfectionScheduleEntity savedEntity = disinfectionScheduleRepository.save(
        disinfectionScheduleEntity);

    Integer hospitalId = disinfectionScheduleEntity.getHospital().getId();
    String sectorName = disinfectionScheduleEntity.getSector().getName();
    LocalDateTime time = disinfectionScheduleEntity.getStartTime();
    // 알람 추가 08시, 방역 시작 10분전, 시작
    notificationService.createNotificationForHospitalUsers(
        hospitalId,
        "금일 " + sectorName + " 방역이 " + time.toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm")) + " 에 존재합니다.", "당일 방역 알람입니다.",
        time.toLocalDate().atTime(8, 0), savedEntity, null);

    notificationService.createNotificationForHospitalUsers(
        hospitalId,
        sectorName + " 방역 10분 전 알람", "방역 10분 전 알람입니다.",
        time.minusMinutes(10), savedEntity, null);

    notificationService.createNotificationForHospitalUsers(
        hospitalId,
        sectorName + " 방역 시작 알람", "방역 시작 알람입니다.",
        time, savedEntity, null);

    return disinfectionScheduleMapper.entityToDto(savedEntity);

  }

  // U
  public DisinfectionScheduleDTO updateDisinfectionSchedule(String uuid,
      DisinfectionScheduleDTO disinfectionScheduleDTO) {
    log.info("updateDisinfectionSchedule {}", disinfectionScheduleDTO);
    DisinfectionScheduleEntity disinfectionScheduleEntity = checkCollectId(uuid,
        disinfectionScheduleDTO);
    DisinfectionScheduleEntity findEntity = disinfectionScheduleRepository.findById(
        disinfectionScheduleDTO.getId()).orElse(null);
    if (findEntity == null) {
      log.info("DisinfectionSchedule not found");
      throw new IllegalArgumentException("DisinfectionSchedule not found");
    }

    findEntity.setSector(disinfectionScheduleEntity.getSector());
    if (!findEntity.getStartTime().equals(disinfectionScheduleEntity.getStartTime())) {
      findEntity.setStartTime(disinfectionScheduleEntity.getStartTime());
      // 기존 알람 삭제
      notificationService.deleteNotificationByDisinfection(findEntity.getId());
      Integer hospitalId = disinfectionScheduleEntity.getHospital().getId();
      String sectorName = disinfectionScheduleEntity.getSector().getName();
      LocalDateTime time = disinfectionScheduleEntity.getStartTime();
      // 알람 추가 08시, 방역 시작 10분전, 시작
      notificationService.createNotificationForHospitalUsers(
          hospitalId,
          "금일 " + sectorName + " 방역이 " + time.toLocalTime()
              .format(DateTimeFormatter.ofPattern("HH:mm")) + " 에 존재합니다.", "당일 방역 알람입니다.",
          time.toLocalDate().atTime(8, 0),findEntity,null);

      notificationService.createNotificationForHospitalUsers(
          hospitalId,
          sectorName + " 방역 10분 전 알람", "방역 10분 전 알람입니다.",
          time.minusMinutes(10),findEntity,null);

      notificationService.createNotificationForHospitalUsers(
          hospitalId,
          sectorName + " 방역 시작 알람", "방역 시작 알람입니다.",
          time,findEntity,null);
    }

    findEntity.setEndTime(disinfectionScheduleEntity.getEndTime());
    findEntity.setDescription(disinfectionScheduleDTO.getDescription());
    findEntity.setStatus(disinfectionScheduleDTO.getStatus());

    DisinfectionScheduleEntity savedEntity = disinfectionScheduleRepository.save(findEntity);
    return disinfectionScheduleMapper.entityToDto(savedEntity);
  }

  // D
  public void deleteDisinfectionSchedule(String uuid,
      Integer disinfectionScheduleId) {
    log.info("deleteDisinfectionSchedule {}", disinfectionScheduleId);
    if (!disinfectionScheduleRepository.existsById(disinfectionScheduleId)) {
      log.info("DisinfectionSchedule not found");
      throw new IllegalArgumentException("DisinfectionSchedule not found");
    }

    // 알람 삭제
    notificationService.deleteNotificationByDisinfection(disinfectionScheduleId);

    disinfectionScheduleRepository.deleteById(disinfectionScheduleId);

  }

  // 방역을 다음 상태로 변경
  public DisinfectionScheduleDTO nextDisinfectionSchedule(String uuid,
      Integer disinfectionScheduleId, DisinfectionStatus status) {
    log.info("nextDisinfectionSchedule {} to {}", disinfectionScheduleId, status);
    DisinfectionScheduleEntity findEntity = disinfectionScheduleRepository.findById(
        disinfectionScheduleId).orElse(null);
    if (findEntity == null) {
      log.info("DisinfectionSchedule not found");
      throw new IllegalArgumentException("DisinfectionSchedule not found");
    }

    HospitalEntity hospital = hospitalService.findHospitalByUuid(uuid);
    if (!findEntity.getHospital().equals(hospital)) {
      log.info("Hospital not equal");
      throw new IllegalArgumentException("Hospital not equal");
    }

    if (findEntity.getStatus().equals(status)) {
      log.info("Status equal");
      throw new IllegalArgumentException("Status equal");
    }

    if (status == DisinfectionStatus.IN_PROGRESS && !findEntity.getStatus()
        .equals(DisinfectionStatus.SCHEDULED)) {
      log.info("DisinfectionSchedule already in progress");
      throw new IllegalArgumentException("DisinfectionSchedule already in progress");
    }

    if ((status == DisinfectionStatus.COMPLETED || status == DisinfectionStatus.FAILED)
        && !findEntity.getStatus().equals(DisinfectionStatus.IN_PROGRESS)) {
      log.info("Wrong schedule status");
      throw new IllegalArgumentException("Wrong schedule status");
    }

    findEntity.setStatus(status);
    if(status == DisinfectionStatus.COMPLETED){
      notificationService.createNotificationForHospitalUsers(findEntity.getHospital().getId(), "방역 완료 알람", "방역이 완료되었습니다.", null, findEntity,null);
    }
    DisinfectionScheduleEntity savedEntity = disinfectionScheduleRepository.save(findEntity);
    return disinfectionScheduleMapper.entityToDto(savedEntity);
  }


  private DisinfectionScheduleEntity checkCollectId(String uuid,
      DisinfectionScheduleDTO disinfectionScheduleDTO) {
    HospitalEntity hospital = hospitalService.findHospitalByUuid(uuid);
    SectorEntity sector = sectorService.findSectorById(disinfectionScheduleDTO.getSector());
    if (sector == null) {
      log.error("sector not found");
      throw new RuntimeException("sector not found");
    }
    if (uuid == null || hospital == null) {
      log.error("checkCollectId failed");
      throw new IllegalArgumentException("checkCollectId failed");
    }
    if (!disinfectionScheduleDTO.getHospital().equals(hospital.getId())) {
      log.warn("disinfectionSchedule id {} not match uuid", disinfectionScheduleDTO.getId());
      throw new IllegalArgumentException(
          "disinfectionSchedule id " + disinfectionScheduleDTO.getId());
    }

    return DisinfectionScheduleEntity.builder()
        .sector(sector)
        .hospital(hospital)
        .description(disinfectionScheduleDTO.getDescription())
        .startTime(disinfectionScheduleDTO.getStartTime())
        .endTime(disinfectionScheduleDTO.getEndTime())
        .build();


  }


}
