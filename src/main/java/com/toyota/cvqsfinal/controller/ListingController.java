package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.*;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import com.toyota.cvqsfinal.service.DefectService;
import com.toyota.cvqsfinal.service.VehicleDefectService;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/listing")
@RequiredArgsConstructor
public class ListingController {

    private final VehicleService vehicleService;
    private final DefectService defectService;

    private final VehicleDefectService vehicleDefectService;
    @PostMapping("/getvehicles")
    ResponseEntity<List<VehicleDto>> getVehicles(@RequestBody GetVehicleParameters getVehicleParameters){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" Send get vehicles request : (getVehicleParameters) "+getVehicleParameters);
        return ResponseEntity.ok(vehicleService.getVehiclesWithPagination(getVehicleParameters));
    }

    @PostMapping("/getdefects")
    ResponseEntity<List<DefectDto>> getDefects(@RequestBody GetDefectParameters getDefectParameters){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" Send get defects request : (getDefectParameters) "+getDefectParameters);
        return ResponseEntity.ok(defectService.getDefectsWithPagination(getDefectParameters));
    }

    @PostMapping("/getvehicledefects")
    ResponseEntity<List<VehicleDefectDto>> getDefects(@RequestBody GetVehicleDefectParameters getVehicleDefectParameters){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" Send get vehicledefects request : (getDefectParameters) "+getVehicleDefectParameters);
        return ResponseEntity.ok(vehicleDefectService.getVehicleDefectsWithPagination(getVehicleDefectParameters));
    }
}
