package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteTypeMapper;
import com.capstone.capstone_server.service.waste.WasteTypeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wsType")
public class WasteTypeUserController {

  private final WasteTypeService wasteTypeService;
  private final WasteTypeMapper wasteTypeMapper;

  @Autowired
  public WasteTypeUserController(WasteTypeService wasteTypeService,
      WasteTypeMapper wasteTypeMapper) {
    this.wasteTypeService = wasteTypeService;
    this.wasteTypeMapper = wasteTypeMapper;
  }

  @Operation(
      summary = "폐기물 종류 리턴",
      description = "유저 권한으로 폐기물의 종류를 받아옵니다."
  )
  @GetMapping()
  public ResponseEntity<List<WasteTypeDTO>> GetAll() {
    List<WasteTypeEntity> typeEntities = wasteTypeService.GetAllWasteTypes();

    List<WasteTypeDTO> typeDTO = wasteTypeMapper.toDTOList(typeEntities);

    return ResponseEntity.ok(typeDTO);
  }
}
