package com.capstone.capstone_server.controller.admin;


import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.service.UserService;
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


  @PatchMapping("/{id}")
  public ResponseEntity<UserDTO> validUser(@PathVariable String id, @RequestBody UserDTO user) {
    UserDTO response = userService.updateUserRole(id, user.getRoles());

    return ResponseEntity.ok(response);
  }
}
