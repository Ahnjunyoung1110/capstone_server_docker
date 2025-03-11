package com.capstone.capstone_server.repository;

import com.capstone.capstone_server.entity.UserEntity;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    public boolean existsById(String id);
    public Optional<UserEntity> findByUsername(String username);
}
