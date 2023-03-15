package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.service.VehicleDefectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/vehicledefect")
@RequiredArgsConstructor
public class VehicleDefectController {

    private final VehicleDefectService vehicleDefectService;


    @PostMapping("/{vehicleId}")
    ResponseEntity<VehicleDefectDto> saveVehicleDefect(@PathVariable Long vehicleId, @RequestBody VehicleDefectDto vehicleDefectDto)throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" VehicleDefect Saved");

        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectSave(vehicleId,vehicleDefectDto);

            return ResponseEntity.ok(vehicleDefectDto1);

    }


    @DeleteMapping("/{vehicleDefectId}")
    ResponseEntity<Void> deleteVehicleDefect(@PathVariable Long vehicleDefectId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+"Send VehicleDefect delete request : (VEHICLE CODE) " + vehicleDefectId);

        boolean vehicleDto = vehicleDefectService.vehicleDefectDel(vehicleDefectId);

        return ResponseEntity.ok().build();


    }

    @PutMapping
    ResponseEntity<VehicleDefectDto> updateVehicleDefect(@RequestBody VehicleDefectDto vehicleDefectDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+"Send VehicleDefect update request : (VEHICLE CODE) " + vehicleDefectDto.getId());

        VehicleDefectDto vehicleDefectDto1 = vehicleDefectService.vehicleDefectUpdate(vehicleDefectDto);

            return ResponseEntity.ok(vehicleDefectDto1);


    }

    @GetMapping("/{vehicleDefectId}")
    ResponseEntity<VehicleDefectDto> getVehicleDefect(@PathVariable Long vehicleDefectId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName()+" GET VEHICLEDEFECT : (VEHICLE CODE) " + vehicleDefectId);

        VehicleDefectDto vehicleDefectDto = vehicleDefectService.vehicleDefectGet(vehicleDefectId);

        return ResponseEntity.ok(vehicleDefectDto);


    }


    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional()
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "Send get image request : (vehicleDefectId) " + id);

        ByteArrayResource byteArrayResource = vehicleDefectService.getImage(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);

    }
}