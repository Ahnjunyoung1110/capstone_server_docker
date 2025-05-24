package com.capstone.capstone_server.service.waste;


import com.capstone.capstone_server.dto.WasteLogDTO;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.entity.WasteEntity;
import com.capstone.capstone_server.entity.WasteLogEntity;
import com.capstone.capstone_server.entity.WasteStatusEntity;
import com.capstone.capstone_server.mapper.WasteLogMapper;
import com.capstone.capstone_server.repository.WasteLogRepository;
import com.capstone.capstone_server.service.user.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WasteLogService {

  private final WasteLogRepository wasteLogRepository;
  private final WasteLogMapper wasteLogMapper;
  private final UserService userService;

  @Autowired
  public WasteLogService(WasteLogRepository wasteLogRepository, WasteLogMapper wasteLogMapper,
      UserService userService) {
    this.wasteLogRepository = wasteLogRepository;
    this.wasteLogMapper = wasteLogMapper;
    this.userService = userService;
  }

  // 폐기물의 상태를 변경할때 마다 log를 기록하는 함수
  public WasteLogEntity createWasteLog(WasteEntity wasteEntity, WasteStatusEntity wasteStatusEntity,
      UserEntity userEntity, String description) {

    WasteLogEntity wasteLogEntity = WasteLogEntity.builder()
        .waste(wasteEntity)
        .status(wasteStatusEntity)
        .user(userEntity)
        .description(description)
        .build();

    log.info("Waste log created {}", wasteLogEntity);
    return wasteLogRepository.save(wasteLogEntity);
  }

  // 폐기물에 대한 로그를 반환하는 함수
  public List<WasteLogDTO> getWasteLog(String wasteId) {
    log.info("Waste log retrieved {}", wasteId);
    List<WasteLogEntity> logEntities = wasteLogRepository.findAllByWasteIdOrderByStatusStatusLevelAsc(
        wasteId);
    List<WasteLogDTO> logDto = wasteLogMapper.entityToDtoList(logEntities);
    for (WasteLogDTO log : logDto) {
      UserEntity user = userService.findByUuid(log.getUserId());
      log.setUserName(user.getUsername());
      log.setName(user.getName());
    }
    return logDto;
  }

}
