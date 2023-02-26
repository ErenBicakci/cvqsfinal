package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.GetVehiclePageable;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.service.JwtService;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final JwtService jwtService;

    @PostMapping("/save")
    ResponseEntity<VehicleDto> saveVehicle(@RequestBody VehicleDto vehicleSaveDto, @RequestHeader(name="Authorization") String token){
        VehicleDto responseObject = vehicleService.vehicleSave(vehicleSaveDto);
        if (responseObject != null){
            log.info(jwtService.findUsername(token.substring(7))+" Vehicle Saved : (VEHICLE CODE) " + vehicleSaveDto.getVehicleCode());
            return ResponseEntity.ok(responseObject);
        }
        log.info(jwtService.findUsername(token.substring(7))+" Vehicle not be saved : (VEHICLE CODE) " + vehicleSaveDto.getVehicleCode());

        return  ResponseEntity.status(400).body(null);
    }

    @GetMapping("/getvehicle/{id}")
    ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id, @RequestHeader(name="Authorization") String token){

        VehicleDto vehicleDto = vehicleService.getVehicleFromId(id);
        if (vehicleDto != null){
            return ResponseEntity.ok(vehicleDto);
        }
        log.info(jwtService.findUsername(token.substring(7))+" Vehicle not found : (VEHICLE CODE) " + id);

        return ResponseEntity.status(400).body(null);

    }

    @DeleteMapping("/deletevehicle/{id}")
    void deleteVehicle(@PathVariable Long id,@RequestHeader (name="Authorization") String token){
        log.info(jwtService.findUsername(token.substring(7))+" Send vehicle delete request : (VEHICLE CODE) " + id);

        if (jwtService.findUsername(token.substring(7)) != null){
            return;
        }
        log.error(jwtService.findUsername(token.substring(7))+" Vehicle Not Found! (id)" + id );

    }

    @PutMapping("/updatevehicle/{id}")
    ResponseEntity<VehicleDto> updateVehicle(@PathVariable Long id,@RequestBody VehicleDto vehicleDto,@RequestHeader (name="Authorization") String token){
        log.info(jwtService.findUsername(token.substring(7))+" Send vehicle update request : (VEHICLE CODE) " + id);

        VehicleDto vehicleDto1 = vehicleService.vehicleUpdate(id,vehicleDto);
        if (vehicleDto1 != null){
            log.info(jwtService.findUsername(token.substring(7))+" Vehicle Updated : (id) " + id);

            return ResponseEntity.ok(vehicleDto1);
        }

        log.info(jwtService.findUsername(token.substring(7))+" Vehicle not found : (id) " + id);

        return ResponseEntity.status(400).body(null);

    }




}
