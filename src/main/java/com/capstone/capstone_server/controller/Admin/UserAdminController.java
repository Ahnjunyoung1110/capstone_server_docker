package com.capstone.capstone_server.controller.admin;


import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {


  private final UserService userService;

  @Autowired
  public UserAdminController(UserService userService) {
    this.userService = userService;
  }


  @Operation(
      summary = "권한 업데이트 ",
      description = "어드민 권한으로 유저의 권한을 업데이트 합니다. user의 roles를 받습니다."
  )
  @PatchMapping("/{id}")
  public ResponseEntity<UserDTO> validUser(@PathVariable String id, @RequestBody UserDTO user) {
    UserDTO response = userService.updateUserRole(id, user.getRoles());

    return ResponseEntity.ok(response);
  }
}
