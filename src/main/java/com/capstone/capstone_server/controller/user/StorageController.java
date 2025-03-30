package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.dto.StorageDTO;
import com.capstone.capstone_server.service.StorageService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("storage")
public class StorageController {

  private final StorageService storageService;

  @Autowired
  public StorageController(StorageService storageService) {
    this.storageService = storageService;
  }

  // 조건에 맞춰 창고를 리턴
  @GetMapping
  public ResponseEntity<List<StorageDTO>> getAllStorage(
      @RequestParam(required = false) Integer hospitalId // 소속 병원 창고 여부
  ) {
    log.info("getAllStorageController");

    // 모든 엔티티 획득
    List<StorageDTO> storageDTOS = storageService.getAllStorages(hospitalId);

    log.info("Find result {}", storageDTOS);

    return ResponseEntity.ok(storageDTOS);
  }

  // 새로운 창고를 생성
  @PostMapping("/createSr")
  public ResponseEntity<StorageDTO> createStorage(@RequestBody StorageDTO storageDTO) {
    log.info("createStorageController");

    // 저장
    StorageDTO createdStorageDTO = storageService.createStorage(storageDTO);
    log.info("Create storage {}", createdStorageDTO);

    return ResponseEntity.ok(createdStorageDTO);
  }

  // 기존 창고를 업데이트
  @PutMapping("/updateSr")
  public ResponseEntity<StorageDTO> updateStorage(@RequestBody StorageDTO storageDTO) {
    log.info("updateStorageController");

    //저장
    StorageDTO updatedStorageDTO = storageService.updateStorage(storageDTO);
    log.info("Update storage {}", updatedStorageDTO);

    return ResponseEntity.ok(updatedStorageDTO);
  }

  // 기존 창고를 삭제(비활성화)
  @DeleteMapping("/deleteSr/{storageId}")
  public ResponseEntity<?> deleteStorage(@PathVariable Integer storageId) {
    log.info("deleteStorageController");

    // 비활성화
    storageService.deleteStorage(storageId);

    log.info("Delete storage {}", storageId);

    return ResponseEntity.ok().build();

  }
}
