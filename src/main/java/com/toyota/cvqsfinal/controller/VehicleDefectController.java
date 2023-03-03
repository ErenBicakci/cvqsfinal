package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.service.VehicleDefectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/vehicledefect")
@RequiredArgsConstructor
public class VehicleDefectController {

    private final VehicleDefectService vehicleDefectService;

    @PostMapping("/save/{vehicleId}/{defectId}")
    ResponseEntity<VehicleDefectDto> saveVehicleDefect(@PathVariable Long vehicleId, @RequestBody VehicleDefectDto vehicleDefectDto)throws Exception{
        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectSave(vehicleId,vehicleDefectDto);
        if (vehicleDefectDto1 != null){
            return ResponseEntity.ok(vehicleDefectDto1);
        }
        return ResponseEntity.status(400).body(null);
    }


    @DeleteMapping("/delete/{vehicleDefectId}")
    void saveVehicleDefect(@PathVariable Long vehicleDefectId)throws Exception{
        boolean vehicleDto = vehicleDefectService.vehicleDefectDel(vehicleDefectId);
        if (vehicleDto){
            return;
        }
    }

    @PutMapping("/update")
    ResponseEntity<VehicleDefectDto> updateVehicleDefect(@RequestBody VehicleDefectDto vehicleDefectDto)throws Exception{
        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectUpdate(vehicleDefectDto);
        if (vehicleDefectDto1 != null){
            return ResponseEntity.ok(vehicleDefectDto1);
        }
        return ResponseEntity.status(400).body(null);
    }

    @GetMapping("/get/{vehicleDefectId}")
    ResponseEntity<VehicleDefectDto> getVehicleDefect(@PathVariable Long vehicleDefectId)throws Exception{
        VehicleDefectDto vehicleDefectDto = vehicleDefectService.vehicleDefectGet(vehicleDefectId);
        if (vehicleDefectDto != null){
            return ResponseEntity.ok(vehicleDefectDto);
        }
        return ResponseEntity.status(400).body(null);
    }
}
