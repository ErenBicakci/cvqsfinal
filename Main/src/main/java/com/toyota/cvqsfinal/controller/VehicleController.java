package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.log.CustomLogInfo;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    @CustomLogInfo
    @PostMapping
    ResponseEntity<VehicleDto> saveVehicle(@RequestBody VehicleDto vehicleSaveDto){

        VehicleDto responseObject = vehicleService.vehicleSave(vehicleSaveDto);
        if (responseObject != null){
            return ResponseEntity.ok(responseObject);
        }
        return  ResponseEntity.status(400).body(null);
    }
    @CustomLogInfo
    @GetMapping("/{id}")
    ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id){

        VehicleDto vehicleDto = vehicleService.getVehicleFromId(id);
        if (vehicleDto != null){
            return ResponseEntity.ok(vehicleDto);
        }
        return ResponseEntity.status(400).body(null);

    }

    @CustomLogInfo
    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> deleteVehicle(@PathVariable Long id){

        boolean bool = vehicleService.vehicleDelete(id);
        if (bool == true){
            return ResponseEntity.ok(true);
        }
         return ResponseEntity.status(400).body(false);
    }

    @CustomLogInfo
    @PutMapping
    ResponseEntity<VehicleDto> updateVehicle(@RequestBody VehicleDto vehicleDto){

        VehicleDto vehicleDto1 = vehicleService.vehicleUpdate(vehicleDto);
        if (vehicleDto1 != null){

            return ResponseEntity.ok(vehicleDto1);
        }
        return ResponseEntity.status(400).body(null);
    }
}