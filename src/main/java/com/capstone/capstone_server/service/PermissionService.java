package com.capstone.capstone_server.service;

import com.capstone.capstone_server.entity.PermissionEntity;
import com.capstone.capstone_server.entity.UserEntity;
import com.capstone.capstone_server.repository.PermissionRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    return permissionRepository.findAllByValidTrueOrderByPermissionLevelAsc();
  }

  // id를 통해 permission을 리턴
  public PermissionEntity getPermission(Integer id) {
    if(id == null){
      log.error("Permission id is null");
      throw new IllegalArgumentException("Permission id is null");
    }
    Optional<PermissionEntity> response = permissionRepository.findById(id);
    if(response.isEmpty()){
      log.error("Permission not found");
      throw new IllegalArgumentException("Permission not found");
    }
    return response.get();
  }


  /*
  신규 권한을 생성한다
   */
  public List<PermissionEntity> createPermission(
      PermissionEntity permissionEntity) {
    // 받은 생성 권한이 없는경우
    if (permissionEntity == null) {
      throw new IllegalArgumentException("Permission entity cannot be null");
    }

    // 일시적으로 권한 레벨을 최하로 설정
    permissionEntity.setPermissionLevel(0);
    permissionRepository.save(permissionEntity);

    return getPermissions();

  }

  /*
  기존 권한의 순서를 설정한다.
   */
  public List<PermissionEntity> updatePermissionLevel(List<PermissionEntity> permissionEntities) {

    // 비어있는 경우
    if (permissionEntities == null) {
      throw new IllegalArgumentException("Permission entities cannot be null");
    }

    // 주어진 순서대로 권한들의 순서를 변경
    int permissionLevel = 1;
    for (PermissionEntity permissionEntity : permissionEntities) {
      permissionEntity.setPermissionLevel(permissionLevel);
      permissionLevel++;
    }

    // 변경 내용을 저장
    permissionRepository.saveAll(permissionEntities);

    // 모든 권한을 리턴
    return getPermissions();
  }

  /*
  기존 권한을 삭제한다
   */

  public List<PermissionEntity> deletePermissionLevel(PermissionEntity permissionEntity) {
    if (permissionEntity == null) {
      throw new IllegalArgumentException("Delete Permission entity cannot be null");
    }

    // 비활성화
    permissionEntity.setValid(false);
    permissionEntity.setPermissionLevel(1000);

    // 활성화된 모든 권한을 리턴
    return getPermissions();

  }

  /*
  해당 요청을 한 유저의 권한이 필요한 권한을 넘는지 확인한다.
   */
  public boolean checkPermission(UserEntity userEntity, PermissionEntity permissionEntity) {
    return userEntity.getPermission().getPermissionLevel() >= permissionEntity.getPermissionLevel();
  }


}
