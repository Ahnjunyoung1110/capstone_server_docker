package com.capstone.capstone_server.controller;

import com.capstone.capstone_server.dto.PermissionDTO;
import com.capstone.capstone_server.entity.PermissionEntity;
import com.capstone.capstone_server.mapper.PermissionMapper;
import com.capstone.capstone_server.service.PermissionService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("permission")

// 유저의 권한을 관리하는 컨트롤러
public class PermissionnController {

  private final PermissionService permissionService;
  private final PermissionMapper permissionMapper;

  @Autowired
  public PermissionnController(PermissionService permissionService,
      PermissionMapper permissionMapper) {
    this.permissionService = permissionService;
    this.permissionMapper = permissionMapper;
  }

  @GetMapping("/getAllPm")
  public List<PermissionEntity> getAllPm() {
    return permissionService.getPermissions();
  }

  @PostMapping("/createPm")
  public ResponseEntity<?> createPm(@RequestBody PermissionDTO permission) {
    PermissionEntity permissionEntity = permissionMapper.toPermissionEntity(permission);

    List<PermissionEntity> createdEntities = permissionService.createPermission(permissionEntity);

    List<PermissionDTO> createdDTO = permissionMapper.permissionEntityToPermissionDTOList(createdEntities);

    return ResponseEntity.ok().body(createdDTO);
  }

  @PutMapping("/updatePm")
  public ResponseEntity<?> updatePm(@RequestBody PermissionDTO permission) {
    PermissionEntity permissionEntity = permissionMapper.toPermissionEntity(permission);

    List<PermissionEntity> updatedEntity = permissionService.createPermission(permissionEntity);

    List<PermissionDTO> updatedDTO = permissionMapper.permissionEntityToPermissionDTOList(updatedEntity);

    return ResponseEntity.ok().body(updatedDTO);
  }

}
