package com.capstone.capstone_server.controller.user;


import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteTypeMapper;
import com.capstone.capstone_server.service.waste.WasteTypeService;
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


  @GetMapping()
  public ResponseEntity<List<WasteTypeDTO>> GetAll() {
    List<WasteTypeEntity> typeEntities = wasteTypeService.GetAllWasteTypes();

    List<WasteTypeDTO> typeDTO = wasteTypeMapper.toDTOList(typeEntities);

    return ResponseEntity.ok(typeDTO);
  }
}
