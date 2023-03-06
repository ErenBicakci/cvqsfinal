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
@RequestMapping("/api/v1/listing")
@RequiredArgsConstructor
public class ListingController {

    private final VehicleService vehicleService;
    private final DefectService defectService;

    private final VehicleDefectService vehicleDefectService;
    @GetMapping("/getvehicles")
    ResponseEntity<List<VehicleDto>> getVehicles(@RequestParam String modelNo,@RequestParam String vehicleCode, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        GetVehicleParameters getVehicleParameters = GetVehicleParameters.builder()
                .modelNo(modelNo)
                .vehicleCode(vehicleCode)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();
        log.info(auth.getName()+" Send get vehicles request : (getVehicleParameters) "+getVehicleParameters);
        return ResponseEntity.ok(vehicleService.getVehiclesWithPagination(getVehicleParameters));
    }

    @GetMapping("/getdefects")
    ResponseEntity<List<DefectDto>> getDefects(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        GetDefectParameters getDefectParameters = GetDefectParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();

        log.info(auth.getName()+" Send get defects request : (getDefectParameters) "+getDefectParameters);
        return ResponseEntity.ok(defectService.getDefectsWithPagination(getDefectParameters));
    }

    @PostMapping("/getvehicledefects")
    ResponseEntity<List<VehicleDefectDto>> getDefects(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType,@RequestParam Long vehicleId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        GetVehicleDefectParameters getVehicleDefectParameters = GetVehicleDefectParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .vehicleId(vehicleId)
                .build();

        log.info(auth.getName()+" Send get vehicledefects request : (getDefectParameters) "+getVehicleDefectParameters);
        return ResponseEntity.ok(vehicleDefectService.getVehicleDefectsWithPagination(getVehicleDefectParameters));
    }
}
