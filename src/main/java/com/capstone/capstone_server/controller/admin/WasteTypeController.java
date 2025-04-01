package com.capstone.capstone_server.controller.admin;


import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteTypeMapper;
import com.capstone.capstone_server.service.waste.WasteTypeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("admin/wsType")
@RestController
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class WasteTypeController {

  private final WasteTypeService wasteTypeService;
  private final WasteTypeMapper wasteTypeMapper;

  @Autowired
  public WasteTypeController(WasteTypeService wasteTypeService, WasteTypeMapper wasteTypeMapper) {
    this.wasteTypeService = wasteTypeService;
    this.wasteTypeMapper = wasteTypeMapper;
  }


  @Operation(
      summary = "폐기물 종류 획득 ",
      description = "어드민 권한으로 모든 폐기물 종류를 반환 합니다."
  )
  @GetMapping()
  public ResponseEntity<List<WasteTypeDTO>> GetAll() {
    List<WasteTypeEntity> typeEntities = wasteTypeService.GetAllWasteTypes();

    List<WasteTypeDTO> typeDTO = wasteTypeMapper.toDTOList(typeEntities);

    return ResponseEntity.ok(typeDTO);
  }

  @Operation(
      summary = "폐기물 종류 생성 ",
      description = "어드민 권한으로 새로운 폐기물 종류를 생성 합니다."
  )
  @PostMapping("/create")
  public ResponseEntity<WasteTypeDTO> CreateWasteType(@RequestBody WasteTypeDTO wasteTypeDTO) {
    WasteTypeDTO response = wasteTypeService.createWasteType(wasteTypeDTO);

    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "폐기물 종류 업데이트 ",
      description = "어드민 권한으로 기존 폐기물 종류 업데이트 합니다."
  )
  @PutMapping("/update/{id}")
  public ResponseEntity<WasteTypeDTO> UpdateWasteType(@PathVariable int id,
      @RequestBody WasteTypeDTO wasteTypeDTO) {
    WasteTypeDTO response = wasteTypeService.updateWasteType(wasteTypeDTO, id);
    return ResponseEntity.ok(response);
  }


  @Operation(
      summary = "폐기물 종류 삭제 ",
      description = "어드민 권한으로 폐기물 종류를 삭제 합니다."
  )
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> DeleteWasteType(@PathVariable int id) {
    wasteTypeService.deleteWasteType(id);
    return ResponseEntity.ok().build();
  }

}
