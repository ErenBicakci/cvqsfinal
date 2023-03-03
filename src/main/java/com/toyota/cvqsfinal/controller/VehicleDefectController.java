package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.service.JwtService;
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
    private final JwtService jwtService;


    @PostMapping("/save/{vehicleId}/{defectId}")
    ResponseEntity<VehicleDefectDto> saveVehicleDefect(@PathVariable Long vehicleId, @RequestBody VehicleDefectDto vehicleDefectDto,@RequestHeader (name="Authorization") String token)throws Exception{
        log.info(jwtService.findUsername(token.substring(7))+" VehicleDefect Saved");

        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectSave(vehicleId,vehicleDefectDto);
        if (vehicleDefectDto1 != null){
            return ResponseEntity.ok(vehicleDefectDto1);
        }
        return ResponseEntity.status(400).body(null);
    }


    @DeleteMapping("/delete/{vehicleDefectId}")
    void deleteVehicleDefect(@PathVariable Long vehicleDefectId,@RequestHeader (name="Authorization") String token){
        log.info(jwtService.findUsername(token.substring(7))+" Send VehicleDefect delete request : (VEHICLEDEFECT CODE) " + vehicleDefectId);

        boolean vehicleDto = vehicleDefectService.vehicleDefectDel(vehicleDefectId);
        if (vehicleDto){
            return;
        }
        log.error(jwtService.findUsername(token.substring(7))+" VEHICLEDEFECT NOT FOUND : (VEHICLE CODE) " + vehicleDefectId);


    }

    @PutMapping("/update")
    ResponseEntity<VehicleDefectDto> updateVehicleDefect(@RequestBody VehicleDefectDto vehicleDefectDto,@RequestHeader (name="Authorization") String token){
        log.info(jwtService.findUsername(token.substring(7))+"Send VehicleDefect update request : (VEHICLE CODE) " + vehicleDefectDto.getId());

        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectUpdate(vehicleDefectDto);
        if (vehicleDefectDto1 != null){
            return ResponseEntity.ok(vehicleDefectDto1);
        }
        log.error(jwtService.findUsername(token.substring(7))+" VEHICLEDEFECT NOT FOUND : (VEHICLE CODE) " + vehicleDefectDto.getId());

        return ResponseEntity.status(400).body(null);
    }

    @GetMapping("/get/{vehicleDefectId}")
    ResponseEntity<VehicleDefectDto> getVehicleDefect(@PathVariable Long vehicleDefectId,@RequestHeader (name="Authorization") String token){
        log.error(jwtService.findUsername(token.substring(7))+" GET VEHICLEDEFECT : (VEHICLE CODE) " + vehicleDefectId);

        VehicleDefectDto vehicleDefectDto = vehicleDefectService.vehicleDefectGet(vehicleDefectId);
        if (vehicleDefectDto != null){
            return ResponseEntity.ok(vehicleDefectDto);
        }
        log.error(jwtService.findUsername(token.substring(7))+" VEHICLEDEFECT NOT FOUND : (VEHICLE CODE) " + vehicleDefectDto.getId());
        return ResponseEntity.status(400).body(null);
    }
}
