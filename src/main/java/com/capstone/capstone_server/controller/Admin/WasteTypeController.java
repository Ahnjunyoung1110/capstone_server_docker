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


  @GetMapping()
  public ResponseEntity<?> GetAll() {
    List<WasteTypeEntity> typeEntities = wasteTypeService.GetAllWasteTypes();

    List<WasteTypeDTO> typeDTO = wasteTypeMapper.toDTOList(typeEntities);

    return ResponseEntity.ok(typeDTO);
  }

  @PostMapping("/create")
  public ResponseEntity<?> CreateWasteType(@RequestBody WasteTypeDTO wasteTypeDTO) {
    WasteTypeDTO response = wasteTypeService.createWasteType(wasteTypeDTO);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> UpdateWasteType(@PathVariable int id, @RequestBody WasteTypeDTO wasteTypeDTO) {
    WasteTypeDTO response = wasteTypeService.updateWasteType(wasteTypeDTO, id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> DeleteWasteType(@PathVariable int id) {
    wasteTypeService.deleteWasteType(id);
    return ResponseEntity.ok().build();
  }

}
