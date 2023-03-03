package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/save")
    ResponseEntity<VehicleDto> saveVehicle(@RequestBody VehicleDto vehicleSaveDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();



        VehicleDto responseObject = vehicleService.vehicleSave(vehicleSaveDto);
        if (responseObject != null){
            log.info(auth.getName()+" Vehicle Saved : (VEHICLE CODE) " + vehicleSaveDto.getVehicleCode());
            return ResponseEntity.ok(responseObject);
        }
        log.info(auth.getName()+" Vehicle not be saved : (VEHICLE CODE) " + vehicleSaveDto.getVehicleCode());

        return  ResponseEntity.status(400).body(null);
    }

    @GetMapping("/getvehicle/{id}")
    ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        VehicleDto vehicleDto = vehicleService.getVehicleFromId(id);
        if (vehicleDto != null){
            return ResponseEntity.ok(vehicleDto);
        }
        log.info(auth.getName()+" Vehicle not found : (VEHICLE CODE) " + id);

        return ResponseEntity.status(400).body(null);

    }

    @DeleteMapping("/deletevehicle/{id}")
    void deleteVehicle(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" Send vehicle delete request : (VEHICLE CODE) " + id);
        vehicleService.vehicleDelete(id);
        if (auth.getName() != null){
            return;
        }
        log.error(auth.getName()+" Vehicle Not Found! (id)" + id );

    }

    @PutMapping("/updatevehicle")
    ResponseEntity<VehicleDto> updateVehicle(@RequestBody VehicleDto vehicleDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" Send vehicle update request : (VEHICLE CODE) " + vehicleDto.getId());

        VehicleDto vehicleDto1 = vehicleService.vehicleUpdate(vehicleDto);
        if (vehicleDto1 != null){
            log.info(auth.getName()+" Vehicle Updated : (id) " + vehicleDto.getId());

            return ResponseEntity.ok(vehicleDto1);
        }

        log.info(auth.getName()+" Vehicle not found : (id) " + vehicleDto.getId());

        return ResponseEntity.status(400).body(null);

    }




}