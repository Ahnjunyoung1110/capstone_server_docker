package com.capstone.capstone_server.controller;

import com.capstone.capstone_server.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("permission")

// 유저의 권한을 관리하는 컨트롤러
public class PermissionnController {

  private final PermissionService permissionService;

  @Autowired
  public PermissionnController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

}
