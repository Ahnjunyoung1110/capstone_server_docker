package com.capstone.capstone_server.service.user;


import com.capstone.capstone_server.dto.UserDTO;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminUserService {

  private final UserService userService;

  public AdminUserService(UserService userService) {
    this.userService = userService;
  }

  public UserDTO createModerateUser(UserDTO userDTO) {
    UserDTO dtos = userService.createUser(userDTO);
    return userService.updateUserRole(dtos.getUuid(), Set.of(RoleType.MODERATOR,
        RoleType.USER));
  }

}
