package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.PermissionEntity;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // 모든 권한 리턴
    public List<PermissionEntity> getPermissions() {
        return permissionRepository.findAll();
    }
    /*
    신규 권한을 생성한다
     */
    public PermissionEntity createPermission(PermissionEntity permissionEntity) {
        // 받은 생성 권한이 없는경우
        if (permissionEntity == null) {
            throw new IllegalArgumentException("Permission entity cannot be null");
        }

        return permissionRepository.save(permissionEntity);
    }

    /*

     */



    /*
    해당 요청을 한 유저의 권한이 필요한 권한을 넘는지 확인한다.
     */
    public boolean checkPermission(UserEntity userEntity, PermissionEntity permissionEntity) {
        return userEntity.getPermission().getPermissionLevel()>= permissionEntity.getPermissionLevel();
    }


}
