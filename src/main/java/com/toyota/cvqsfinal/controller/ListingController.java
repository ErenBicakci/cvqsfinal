package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.GetVehiclePageable;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/listing")
@RequiredArgsConstructor
public class ListingController {

    private final VehicleService vehicleService;
    @PostMapping("/getvehicles")
    ResponseEntity<List<Vehicle>> getVehicles(@RequestBody GetVehiclePageable getVehiclePageable, @RequestHeader(name="Authorization") String token){
        return ResponseEntity.ok(vehicleService.getVehiclesWithPagination(getVehiclePageable));
    }
}
