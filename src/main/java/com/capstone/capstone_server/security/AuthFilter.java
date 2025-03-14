package com.capstone.capstone_server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class AuthFilter extends OncePerRequestFilter {


    private final TokenProvider tokenProvider;

    public AuthFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        logger.info("Filtering request");


        // 토큰 가져오기
        String token = request.getHeader("Authorization");

        // 헤더에 Bearer 토큰이 존재하는 경우
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String UUid = tokenProvider.validateToken(token);

            // 인증된 토큰인 경우
            if (UUid != null) {
                logger.info("user UUId: " + UUid);
                // 사용자의 인증 정보를 Spring Security에 등록
                AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(UUid, null, AuthorityUtils.NO_AUTHORITIES);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);

            }
            // 인증되지 않은 토큰의 경우
            else{
                logger.error("Invalid token");
                throw new ServletException("Invalid token");
            }


        }
        logger.info("Filtering end");
        // 다음 필터를 호출 또는 컨트롤러로 요청 전달
        filterChain.doFilter(request, response);
    }

}
