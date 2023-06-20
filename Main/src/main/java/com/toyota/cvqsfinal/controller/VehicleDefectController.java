package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.log.CustomLogInfo;
import com.toyota.cvqsfinal.log.CustomLogInfoWithoutParameters;
import com.toyota.cvqsfinal.service.VehicleDefectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/vehicledefect")
@RequiredArgsConstructor
public class VehicleDefectController {

    private final VehicleDefectService vehicleDefectService;

    @CustomLogInfo
    @PostMapping("/{vehicleId}")
    ResponseEntity<VehicleDefectDto> saveVehicleDefect(@PathVariable Long vehicleId, @RequestBody VehicleDefectDto vehicleDefectDto){

        vehicleDefectDto.setId(vehicleId);
        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectSave(vehicleDefectDto, vehicleId);
        return ResponseEntity.ok(vehicleDefectDto1);

    }

    @CustomLogInfo
    @DeleteMapping("/{vehicleDefectId}")
    ResponseEntity<Boolean> deleteVehicleDefect(@PathVariable Long vehicleDefectId){


        boolean vehicleDto = vehicleDefectService.vehicleDefectDel(vehicleDefectId);

        if (!vehicleDto){
            return ResponseEntity.status(400).body(false);
        }
        return ResponseEntity.ok(true);


    }

    @CustomLogInfo
    @PutMapping
    ResponseEntity<VehicleDefectDto> updateVehicleDefect(@RequestBody VehicleDefectDto vehicleDefectDto){

        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectUpdate(vehicleDefectDto);
        return ResponseEntity.ok(vehicleDefectDto1);
    }

    @CustomLogInfo
    @GetMapping("/{vehicleDefectId}")
    ResponseEntity<VehicleDefectDto> getVehicleDefect(@PathVariable Long vehicleDefectId){

        VehicleDefectDto vehicleDefectDto = vehicleDefectService.vehicleDefectGet(vehicleDefectId);
        return ResponseEntity.ok(vehicleDefectDto);
    }

    @CustomLogInfoWithoutParameters
    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional()
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {

        ByteArrayResource byteArrayResource = vehicleDefectService.getImage(id);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);
    }
}