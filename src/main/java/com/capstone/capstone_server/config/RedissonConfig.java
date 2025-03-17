package com.capstone.capstone_server.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {
    Config config = new Config();
    // Redis 서버 주소와 포트를 설정합니다.
    config.useSingleServer().setAddress("redis://redis:6379");
    return Redisson.create(config);
  }
}
