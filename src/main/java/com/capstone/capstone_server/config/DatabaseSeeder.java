package com.capstone.capstone_server.config;

import com.capstone.capstone_server.entity.RoleEntity;
import com.capstone.capstone_server.entity.RoleEntity.RoleType;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.RoleRepository;
import com.capstone.capstone_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final RedissonClient redissonClient;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // lock을 생성

    RLock lock = redissonClient.getLock("seeder-lock");
    boolean isLocked = false;

    try {
      isLocked = lock.tryLock(10, 5 * 60, TimeUnit.SECONDS);
      if (isLocked) {
        // Admin이 있는지 확인
        Optional<RoleEntity> admin = roleRepository.findByName(RoleType.ADMIN);

        // 없다면 생성후 adminRole에 저장
        RoleEntity adminRole = admin.orElseGet(() -> {
          RoleEntity newAdminRole = RoleEntity.builder().name(RoleType.ADMIN).description("어드민")
              .build();
          log.info("Admin role created");
          return roleRepository.save(newAdminRole);
        });

        // User role 의 존재 확인후 없다면 생성
        Optional<RoleEntity> user = roleRepository.findByName(RoleType.USER);

        RoleEntity userRole = user.orElseGet(() -> {
          RoleEntity newUserRole = RoleEntity.builder().name(RoleType.USER).description("일반 유저")
              .build();
          log.info("✅ USER Role created");
          return roleRepository.save(newUserRole);
        });

        Optional<RoleEntity> moderator = roleRepository.findByName(RoleType.MODERATOR);

        RoleEntity moderatorRole = user.orElseGet(() -> {
          RoleEntity newUserRole = RoleEntity.builder().name(RoleType.MODERATOR).description("중간 관리자")
              .build();
          log.info("✅ MODERATOR Role created");
          return roleRepository.save(newUserRole);
        });

        Optional<RoleEntity> warehouse = roleRepository.findByName(RoleType.WAREHOUSE_MANAGER);

        RoleEntity warehouseRole = user.orElseGet(() -> {
          RoleEntity newUserRole = RoleEntity.builder().name(RoleType.WAREHOUSE_MANAGER).description("창고 관리자")
              .build();
          log.info("✅ WAREHOUSE_MANAGER Role created");
          return roleRepository.save(newUserRole);
        });

        // 최초의 유저를 어드민으로 생성
        if (userRepository.findByUsername("admin").isEmpty()) {
          UserEntity adminUser = UserEntity.builder()
              .username("admin")
              .password(passwordEncoder.encode("admin"))
              .name("ADMIN")
              .email("example@naver.com")
              .hospital(null)
              .createdAt(new Date())
              .updatedAt(new Date())
              .valid(true)
              .roles(new HashSet<>(Set.of(adminRole, userRole, moderatorRole, warehouseRole)))
              .build();

          userRepository.save(adminUser);
          adminUser.setValid(true);
          userRepository.save(adminUser);
          log.info("Admin created");
        }
      } else {
        log.warn("다른 인스턴스에서 실행중");
      }
    } catch (InterruptedException e) {
      log.error("락 획득중 에러", e);
      Thread.currentThread().interrupt();
    } finally {
      if (isLocked && lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }

  }
}
