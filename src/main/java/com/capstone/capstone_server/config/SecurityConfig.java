package com.capstone.capstone_server.config;

import com.capstone.capstone_server.security.AdminFilter;
import com.capstone.capstone_server.security.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  final
  AuthFilter authFilter;
  AdminFilter adminFilter;

  public SecurityConfig(AuthFilter authFilter, AdminFilter adminFilter) {
    this.authFilter = authFilter;
    this.adminFilter = adminFilter;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/hospital/**", "/permission/**") // 🔥 이 부분을 명확하게 requestMatchers로 변경
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/hospital/**", "/permission/**").authenticated()
            .anyRequest().permitAll())  // 🛑 다른 요청은 여기에 포함되지 않도록 설정
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(AbstractHttpConfigurer::disable);

    http.addFilterBefore(adminFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/", "/auth/**").permitAll()
            .anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(AbstractHttpConfigurer::disable);

    http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}

