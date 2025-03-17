package com.capstone.capstone_server.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AdminFilter extends OncePerRequestFilter {

  @Value("${adminkey}")
  private String adminKey;


  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    String path = httpServletRequest.getRequestURI();


    if (path.startsWith("/hospital") || path.startsWith("/permission/")) {
      logger.info("AdminFilter Request");
      String requestKey = httpServletRequest.getHeader("X-ADMIN-KEY");
      if (adminKey.equals(requestKey)) {
        logger.info("AdminFilter Request confirm");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
      } else {
        logger.warn("AdminFilter Request denied");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
