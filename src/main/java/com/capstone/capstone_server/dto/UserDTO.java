package com.capstone.capstone_server.dto;

import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

  private String token;
  private String fcmToken;
  private String username;
  private String password;
  private String uuid;
  private String email;
  private String phoneNumber;
  private String name;
  private Integer hospitalId;
  private Set<RoleType> roles;
  private RoleType primaryRole;
}
