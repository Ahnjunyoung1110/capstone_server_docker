package com.capstone.capstone_server.controller.admin;


import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import com.capstone.capstone_server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("admin/user")
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
public class UserAdminController {


  private final UserService userService;

  @Autowired
  public UserAdminController(UserService userService) {
    this.userService = userService;
  }


  @Operation(
      summary = "권한 업데이트 ",
      description = "어드민 권한 또는 중간관리자의 권한으로 유저의 권한을 업데이트 합니다. 또한 활성화합니다. user의 roles를 받습니다."
          + "필수 param: hospitalName, Set<RoleType> roles;"
  )
  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> validUser(@PathVariable String id, @RequestBody UserDTO user) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin && user.getRoles().contains(RoleType.MODERATOR)) {
      log.warn("Moderator can not assign moderator role to user");
      throw new IllegalArgumentException("Moderator can not assign moderator role to user");
    }
    UserDTO response = userService.updateUserRole(id, user.getRoles());

    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "유저 비활성화",
      description = "유저를 비활성화합니다."
  )
  @PutMapping("/invalid/{id}")
  public ResponseEntity<?> invalidUser(@AuthenticationPrincipal CustomUserDetails user,
      @PathVariable String id) {
    log.info("invalidUser {}", id);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    UserDTO response;
    if (!isAdmin) {
      log.info("Invalid by moderator");
      response = userService.invalidUser(user.getUsername(), id);
    } else {
      log.info("Invalid by admin");
      response = userService.invalidUserByAdmin(id);
    }
    return ResponseEntity.ok(response);
  }


  @Operation(
      summary = "유저 리턴",
      description = "어드민 권한일시 전체 유저, 중간관리자 일시 해당 병원의 유저만 리턴합니다."
  )
  @GetMapping
  public ResponseEntity<List<UserDTO>> getUser(@AuthenticationPrincipal CustomUserDetails detail) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    log.info("get All Users by {}", auth.getName());

    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    List<UserDTO> response;
    if (!isAdmin) {
      log.info("Find User In the same hospital");
      response = userService.findUserInSameHospital(detail.getUsername());
    } else {
      log.info("Find all user by admin");
      response = userService.findUserAll();
    }

    return ResponseEntity.ok().body(response);
  }


  @Operation(
      summary = "유저 수정",
      description = "유저의 정보를 수정합니다. DTO 내부에 id, name, email, phoneNumber가 모두 필요합니다."
  )
  @PutMapping("/update/{id}")
  public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal CustomUserDetails details,
      @PathVariable String id, @RequestBody UserDTO user) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    UserDTO response = userService.updateUserByAdmin(user, details.getUsername(), id, isAdmin);
    return ResponseEntity.ok(response);
  }
}
