package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.*;
import com.toyota.cvqsfinal.log.CustomLogInfo;
import com.toyota.cvqsfinal.service.DefectService;
import com.toyota.cvqsfinal.service.VehicleDefectService;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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

    @CustomLogInfo
    @GetMapping("/getvehicles")
    ResponseEntity<List<VehicleDto>> getVehicles(@RequestParam String modelNo,@RequestParam String vehicleCode, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){
        GetVehicleParameters getVehicleParameters = GetVehicleParameters.builder()
                .modelNo(modelNo)
                .vehicleCode(vehicleCode)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();
        return ResponseEntity.ok(vehicleService.getVehiclesWithPagination(getVehicleParameters));
    }

    @CustomLogInfo
    @GetMapping("/getdefects")
    ResponseEntity<List<DefectDto>> getDefects(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){

        GetDefectParameters getDefectParameters = GetDefectParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();

        return ResponseEntity.ok(defectService.getDefectsWithPagination(getDefectParameters));
    }
    @CustomLogInfo
    @GetMapping("/getvehicledefectsfromvehicle")
    ResponseEntity<List<VehicleDefectDto>> getVehicleDefectsFromVehicle(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType,@RequestParam Long vehicleId){

        GetVehicleDefectParameters getVehicleDefectParameters = GetVehicleDefectParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .vehicleId(vehicleId)
                .build();
        return ResponseEntity.ok(vehicleDefectService.getVehicleDefectsFromVehicleWithPagination(getVehicleDefectParameters));
    }


    @CustomLogInfo
    @GetMapping("/getvehicledefects")
    ResponseEntity<List<VehicleDefectDto>> getVehicleDefects(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){

        GetVehicleDefectParameters getVehicleDefectParameters = GetVehicleDefectParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();
        return ResponseEntity.ok(vehicleDefectService.getVehicleDefectsWithPagination(getVehicleDefectParameters));
    }





}
