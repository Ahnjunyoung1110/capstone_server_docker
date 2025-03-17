package com.capstone.capstone_server.detail;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


// Spring Security가 user을 처리하기 위한 클래스
public class CustomUserDetails implements UserDetails {

  private final String uuid;
  private final List<GrantedAuthority> authorities;

  public CustomUserDetails(String uuid, List<String> roles) {
    this.uuid = uuid;
    this.authorities = roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getUsername() {
    return uuid;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
