package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.GetVehiclePageable;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.service.DefectService;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/listing")
@RequiredArgsConstructor
public class ListingController {

    private final VehicleService vehicleService;
    private final DefectService defectService;
    @PostMapping("/getvehicles")
    ResponseEntity<List<Vehicle>> getVehicles(@RequestBody GetVehiclePageable getVehiclePageable, @RequestHeader(name="Authorization") String token){
        return ResponseEntity.ok(vehicleService.getVehiclesWithPagination(getVehiclePageable));
    }

    @PostMapping("/getdefects")
    ResponseEntity<List<Vehicle>> getDefects(@RequestBody GetVehiclePageable getVehiclePageable, @RequestHeader(name="Authorization") String token){
        return ResponseEntity.ok(vehicleService.getVehiclesWithPagination(getVehiclePageable));
    }
}
