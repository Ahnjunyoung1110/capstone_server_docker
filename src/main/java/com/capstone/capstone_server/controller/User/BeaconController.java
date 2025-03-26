package com.capstone.capstone_server.controller.User;


import com.capstone.capstone_server.dto.BeaconDTO;
import com.capstone.capstone_server.service.BeaconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("beacon")
public class BeaconController {

  BeaconService beaconService;

  @Autowired
  public BeaconController(BeaconService beaconService) {
    this.beaconService = beaconService;
  }


  @GetMapping
  public ResponseEntity<?> getAllBeacons() {
    log.info("getAllBeacons Controller");

    return ResponseEntity.ok(beaconService.getAllBeacons());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBeaconById(@PathVariable("id") int id) {
    log.info("getBeaconById Controller");
    return ResponseEntity.ok(beaconService.getBeaconById(id));
  }

  @PostMapping("/createBc")
  public ResponseEntity<?> createBeacon(@RequestBody BeaconDTO beaconDTO) {
    log.info("createBeacon Controller");
    return ResponseEntity.ok(beaconService.createBeacon(beaconDTO));
  }

  @PutMapping("/updateBc/{id}")
  public ResponseEntity<?> updateBeacon(@PathVariable("id") int id, @RequestBody BeaconDTO beaconDTO) {
    log.info("updateBeacon Controller");
    return ResponseEntity.ok(beaconService.updateBeacon(id, beaconDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBeacon(@PathVariable("id") int id) {
    log.info("deleteBeacon Controller");
    beaconService.deleteBeacon(id);
    return ResponseEntity.ok().build();
  }
}
