package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.GetVehiclePageable;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Vehicle;
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

    @PostMapping("/save")
    ResponseEntity<VehicleDto> saveVehicle(@RequestBody VehicleDto vehicleSaveDto, @RequestHeader(name="Authorization") String token){
        VehicleDto responseObject = vehicleService.vehicleSave(vehicleSaveDto,token.substring(7));
        if (responseObject != null){
            return ResponseEntity.ok(responseObject);
        }
        return  ResponseEntity.status(400).body(null);
    }

    @GetMapping("/getvehicle/{id}")
    ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id){
        return ResponseEntity.ok(vehicleService.getVehicleFromId(id));
    }

    @DeleteMapping("/deletevehicle/{id}")
    void deleteVehicle(@PathVariable Long id,@RequestHeader (name="Authorization") String token){
        vehicleService.vehicleDelete(id,token.substring(7));
    }

    @PutMapping("/updatevehicle/{id}")
    void updateVehicle(@PathVariable Long id,@RequestBody VehicleDto vehicleDto,@RequestHeader (name="Authorization") String token){
        vehicleService.vehicleUpdate(id,vehicleDto,token.substring(7));
    }




}
