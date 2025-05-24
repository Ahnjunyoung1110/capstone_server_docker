package com.capstone.capstone_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {



    // cors 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        final long MAX_AGE_SECS = 3600;
        registry.addMapping("/**")
                .allowedOrigins("http://192.168.45.192:3000")
                .allowedOrigins("http://localhost:3000") // 해당 주소에서의 요청만 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 해당 요청 종류만 허용
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS); //1시간 동안 캐시 유지
    }


    // 패스워드 해싱용 함수
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
