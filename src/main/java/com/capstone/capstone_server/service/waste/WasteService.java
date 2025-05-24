package com.capstone.capstone_server.service.waste;

import com.capstone.capstone_server.dto.WasteDTO;
import com.capstone.capstone_server.entity.BeaconEntity;
import com.capstone.capstone_server.entity.HospitalEntity;
import com.capstone.capstone_server.entity.StorageEntity;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteMapper;
import com.capstone.capstone_server.repository.WasteRepository;
import com.capstone.capstone_server.service.BeaconService;
import com.capstone.capstone_server.service.HospitalService;
import com.capstone.capstone_server.service.NotificationService;
import com.capstone.capstone_server.service.StorageService;
import com.capstone.capstone_server.service.user.UserService;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WasteService {

  private final WasteRepository wasteRepository;
  private final HospitalService hospitalService;
  private final WasteStatusService wasteStatusService;
  private final WasteTypeService wasteTypeService;
  private final BeaconService beaconService;
  private final StorageService storageService;
  private final WasteMapper wasteMapper;
  private final WasteLogService wasteLogService;
  private final UserService userService;
  private final NotificationService notificationService;

  @Autowired
  public WasteService(WasteRepository wasteRepository, HospitalService hospitalService,
      WasteStatusService wasteStatusService, WasteTypeService wasteTypeService,
      BeaconService beaconService, StorageService storageService, WasteMapper wasteMapper,
      WasteLogService wasteLogService,
      UserService userService, NotificationService notificationService) {
    this.wasteRepository = wasteRepository;
    this.hospitalService = hospitalService;
    this.wasteStatusService = wasteStatusService;
    this.wasteTypeService = wasteTypeService;
    this.beaconService = beaconService;
    this.storageService = storageService;
    this.wasteMapper = wasteMapper;
    this.wasteLogService = wasteLogService;
    this.userService = userService;
    this.notificationService = notificationService;
  }


  // 병원에 관계 없이 DB에 등록된 모든 폐기물을 리턴
  public List<WasteEntity> getAllWastes() {
    return wasteRepository.findAll();
  }

  // 상세검색
  public List<WasteEntity> getAllWasteEverything(Boolean valid, String uuid, String wasteId,
      Integer beaconId, Integer wasteTypeId, Integer wasteStatusId, Integer storageId,
      Date startDate, Date endDate) {
    // uuid를 통해 병원의 폐기물을 검색 후 리턴
    List<WasteEntity> wastes = wasteRepository.findWasteEntityEverything(valid, wasteId, uuid,
        beaconId, wasteTypeId, wasteStatusId, storageId, startDate, endDate);

    // 등록된 폐기물이 존재하지 않을경우
    if (wastes == null || wastes.isEmpty()) {
      throw new IllegalArgumentException("검색 내역이 존재하지 않습니다.");
    }
    return wastes;
  }

  // 폐기물 pk를 통해 검색
  public WasteDTO findWasteById(String id) {
    log.info("findWasteById");
    WasteEntity waste = wasteRepository.findById(id).orElse(null);
    if (waste == null) {
      log.warn("No waste found with id {}", id);
      throw new IllegalArgumentException("No waste found with id " + id);
    }

    return wasteMapper.toWasteDTO(waste);
  }

  // 폐기물 생성
  public WasteDTO createWaste(WasteDTO wasteDTO, String uuid) {
    WasteEntity wasteEntity = dtoToEntity(wasteDTO);
    wasteEntity.setId(generateNextId());
    log.info("create waste : {}", wasteEntity);

    if (wasteEntity.getBeacon().isUsed()) {
      log.info("beacon is used");
      throw new IllegalArgumentException("beacon is used");
    }

    UserEntity userEntity = userService.findByUuid(uuid);
    if (!wasteEntity.getBeacon().getHospital().equals(userEntity.getHospital())) {
      log.warn("hospital not match with user");
      throw new IllegalArgumentException("hospital not match with user");
    }

    wasteEntity.getBeacon().setUsed(Boolean.TRUE);
    WasteEntity responseEntity = wasteRepository.save(wasteEntity);

    WasteStatusEntity wasteStatus = wasteStatusService.getWasteStatusEntity(
        wasteDTO.getWasteStatusId());

    wasteLogService.createWasteLog(wasteEntity, wasteStatus, userEntity,
        wasteEntity.getDescription());

    LocalDateTime now = LocalDateTime.now();
    Integer period = wasteEntity.getWasteType().getPeriod();
    LocalDateTime targetTime = getLocalDateTime(now, period);
    // 알람을 생성
    notificationService.createNotificationForHospitalUsers(userEntity.getHospital().getId(),
        "보관 기일 임박 알람",
        now.plusDays(period).format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
            + " 에 폐기물 보관 기일이 만료합니다.", targetTime, null, wasteEntity);
    return wasteMapper.toWasteDTO(responseEntity);
  }


  private static LocalDateTime getLocalDateTime(LocalDateTime now, Integer period) {
    LocalDateTime targetTime = now.plusDays(period).minusDays(2);

    // 만약 알람 시간이 오늘이고, 10시가 넘었다면 다음날 08시로 설정
    if (targetTime.getHour() >= 22) { // 22시 넘어가면 너무 늦다고 판단
      targetTime = targetTime.plusDays(1).toLocalDate().atTime(8, 0); // 다음날 08:00
    } else {
      // 8시 이전이면 8시로 설정
      targetTime = targetTime.withHour(8).withMinute(0).withSecond(0).withNano(0);
    }
    return targetTime;
  }

  private String generateNextId() {
    String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
    String prefix = today;

    String maxId = wasteRepository.findMaxIdLike(prefix + "%");
    int next = 1;
    if (maxId != null) {
      String numberPart = maxId.substring(8);
      next = Integer.parseInt(numberPart) + 1;
    }
    return prefix + String.format("%03d", next);
  }


  // 폐기물의 비콘을 제거하는 함수
  public WasteDTO eliminateBeacon(String wasteId) {
    log.info("eliminateBeacon");
    WasteEntity waste = wasteRepository.findById(wasteId).orElse(null);
    if (waste == null) {
      log.warn("No waste found with id {}", wasteId);
      throw new IllegalArgumentException("No waste found with id " + wasteId);
    }

    waste.getBeacon().setUsed(Boolean.FALSE);
    waste.setBeacon(null);

    WasteEntity responseEntity = wasteRepository.save(waste);

    return wasteMapper.toWasteDTO(responseEntity);
  }

  // DeviceAddress LIST를 가지고 해당 WasteEntity List를 리턴
  public List<WasteDTO> getWasteByBeaconMac(List<String> beaconMac) {
    log.info("getWasteByBeaconMac, {}", beaconMac);
    if (beaconMac == null || beaconMac.isEmpty()) {
      log.warn("beaconMac is null or empty");
      throw new IllegalArgumentException("beaconMac is null or empty");
    }

    List<BeaconEntity> beaconEntities = beaconService.getAllBeaconsByMacAddress(beaconMac);
    if (beaconEntities == null || beaconEntities.isEmpty()) {
      log.warn("beaconEntities is null or empty");
      throw new IllegalArgumentException("beaconEntities is null or empty");
    }
    List<WasteEntity> wasteEntities = wasteRepository.findAllByBeaconIn(beaconEntities);
    if (wasteEntities == null || wasteEntities.isEmpty()) {
      log.warn("wasteEntities is null or empty");
      throw new IllegalArgumentException("wasteEntities is null or empty");
    }
    return wasteMapper.toDTOList(wasteEntities);
  }

  // 폐기물 수정
  public WasteDTO updateWaste(WasteDTO wasteDTO, String id, String uuid) {
    WasteEntity wasteEntity = dtoToEntity(wasteDTO);

    WasteEntity updateEntity = wasteRepository.findById(id).orElse(null);
    if (updateEntity == null) {
      log.error("Cannot update waste : {}", wasteEntity);
      throw new IllegalArgumentException("Cannot update waste");
    }

    log.info("update waste : {}", wasteEntity);

    // 기존 비콘과 다른경우
    if (!wasteEntity.getBeacon().getId()
        .equals(updateEntity.getBeacon().getId())) {
      // 비콘의 소속 병원이 유저의 병원과 다른경우
      if (!wasteEntity.getBeacon().getHospital().equals(updateEntity.getHospital())) {
        log.warn("Hospital mismatch");
        throw new IllegalArgumentException("Hospital mismatch");
      }
      wasteEntity.getBeacon().setUsed(Boolean.FALSE);
      // 이미 사용중인 비콘으로 변경하려 한 경우
      if (wasteEntity.getBeacon().isUsed()) {
        log.warn("Beacon is already Used");
        throw new IllegalArgumentException("Beacon is already Used");
      }
      updateEntity.setBeacon(wasteEntity.getBeacon());
      updateEntity.getBeacon().setUsed(Boolean.TRUE);
    }

    // 기존 폐기물 Type과 다른경우ㅜ
    if (!wasteEntity.getWasteType().getId().equals(updateEntity.getWasteType().getId())) {

      // 기존 알람 변경
      notificationService.deleteNotificationByWaste(wasteEntity);

      LocalDateTime now = LocalDateTime.now();
      LocalDateTime createdAt = updateEntity.getCreatedAt().toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
      Integer period = wasteEntity.getWasteType().getPeriod();
      // 폐기물 종류 변경시 이미 보관 기일이 만료된 경우
      if (createdAt.plusDays(period).isBefore(now)) {
        log.warn("이미 보관 기일이 지난 타입으로 변경하려 시도하고 있습니다.");
        throw new IllegalArgumentException("이미 보관 기일이 지난 타입으로 변경하려 시도하고 있습니다.");
      }

      updateEntity.setWasteType(wasteEntity.getWasteType());
      LocalDateTime targetTime = getLocalDateTime(createdAt, period);
      // 알람을 생성
      notificationService.createNotificationForHospitalUsers(updateEntity.getHospital().getId(),
          "보관 기일 임박 알람",
          now.plusDays(period).format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
              + " 에 폐기물 보관 기일이 만료합니다.", targetTime, null, wasteEntity);
    }

    updateEntity.setDescription(wasteDTO.getDescription());
    // 창고 변경
    updateEntity.setStorage(wasteEntity.getStorage());
    wasteRepository.save(updateEntity);

    UserEntity logUser = userService.findByUuid(uuid);

    wasteLogService.createWasteLog(updateEntity, updateEntity.getWasteStatus(), logUser,
        wasteDTO.getDescription());

    return wasteMapper.toWasteDTO(updateEntity);
  }

  // 폐기물을 다음단계로 변경
  public WasteDTO toNextStatus(String uuid, String wasteId, String description,
      Boolean isWarehouseManager) {
    WasteEntity waste = wasteRepository.findById(wasteId).orElse(null);
    if (waste == null) {
      log.warn("No waste found with id {}", wasteId);
      throw new IllegalArgumentException("No waste found with id " + wasteId);
    }
    log.info("Change : {}", waste);
    WasteStatusEntity wasteStatus = wasteStatusService.transportTrue(waste.getWasteStatus(), +1);

    log.info("To : {}", wasteStatus);
    BeaconEntity beacon = waste.getBeacon();

    if (wasteStatusService.checkFinal(wasteStatus)) {
      if (!isWarehouseManager) {
        log.warn("Waste can not be final status without warehouse manager's check");
        throw new IllegalArgumentException(
            "Waste can not be final status without warehouse manager's check");
      }
      beacon.setUsed(Boolean.FALSE);
      waste.setValid(false);
      waste.setBeacon(null);
    } else {
      beacon.setUsed(Boolean.TRUE);
    }

    waste.setBeacon(beacon);
    waste.setWasteStatus(wasteStatus);
    wasteRepository.save(waste);

    UserEntity logUser = userService.findByUuid(uuid);

    wasteLogService.createWasteLog(waste, waste.getWasteStatus(), logUser, description);

    return wasteMapper.toWasteDTO(waste);

  }

  // 폐기물 삭제
  public void deleteWaste(String id) {
    WasteEntity wasteEntity = wasteRepository.findById(id).orElse(null);
    if (wasteEntity == null) {
      log.error("Cannot delete waste : {}", id);
      throw new IllegalArgumentException("Cannot delete waste");
    }

    log.info("delete waste : {}", wasteEntity);
    wasteEntity.setValid(false);
    wasteEntity.getBeacon().setUsed(Boolean.FALSE);

    notificationService.deleteNotificationByWaste(wasteEntity);
    wasteRepository.save(wasteEntity);
  }


  // 폐기물 DTO를 Entity로 변환하는 함수
  public WasteEntity dtoToEntity(WasteDTO wasteDTO) {
    if (wasteDTO == null) {
      log.error("waste DTO cannot be null");
      throw new IllegalArgumentException("waste DTO cannot be null");
    }

    // 병원 검색
    HospitalEntity hospital = hospitalService.getHospitalById(wasteDTO.getHospitalId());
    if (hospital == null) {
      log.error("hospital not found");
      throw new IllegalArgumentException("hospital not found");
    }

    // 저장 창고 검색
    StorageEntity storage = storageService.getStorageById(wasteDTO.getStorageId());
    if (storage == null) {
      log.error("storage not found");
      throw new IllegalArgumentException("storage not found");
    }

    // 비콘 검색
    BeaconEntity beacon = beaconService.getBeaconEntityById(wasteDTO.getBeaconId());
    if (beacon == null) {
      log.error("beacon not found");
      throw new IllegalArgumentException("beacon not found");
    }

    // wasteType 검색
    WasteTypeEntity wasteType = wasteTypeService.GetWasteTypeById(wasteDTO.getWasteTypeId());
    if (wasteType == null) {
      log.error("waste type not found");
      throw new IllegalArgumentException("waste type not found");
    }

    // wasteStatus 검색
    WasteStatusEntity wasteStatus = wasteStatusService.getWasteStatusEntity(
        wasteDTO.getWasteStatusId());
    if (wasteStatus == null) {
      log.error("waste status not found");
      throw new IllegalArgumentException("waste status not found");
    }

    return WasteEntity.builder()
        .id(wasteDTO.getId())
        .hospital(hospital)
        .storage(storage)
        .beacon(beacon)
        .wasteType(wasteType)
        .wasteStatus(wasteStatus)
        .description(wasteDTO.getDescription())
        .build();

  }
}
