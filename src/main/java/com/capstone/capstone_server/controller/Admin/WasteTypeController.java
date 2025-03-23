package com.capstone.capstone_server.controller.Admin;


import com.capstone.capstone_server.dto.WasteTypeDTO;
import com.capstone.capstone_server.entity.WasteTypeEntity;
import com.capstone.capstone_server.mapper.WasteTypeMapper;
import com.capstone.capstone_server.service.Waste.WasteTypeService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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


  @GetMapping()
  public ResponseEntity<?> GetAll() {
    List<WasteTypeEntity> typeEntities = wasteTypeService.GetAllWasteTypes();

    List<WasteTypeDTO> typeDTO = wasteTypeMapper.toDTOList(typeEntities);

    return ResponseEntity.ok(typeDTO);
  }

}
