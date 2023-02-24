package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final JwtService jwtService;

    public VehicleDto vehicleSave(VehicleDto vehicleSaveDto, String token){
        if (vehicleRepository.findByCodeAndDeletedFalse(vehicleSaveDto.getVehicleCode()) == null){
            log.info(jwtService.findUsername(token)+" Vehicle Saved : (VEHICLE CODE) " + vehicleSaveDto.getVehicleCode());
            vehicleRepository.save(Vehicle.builder().code(vehicleSaveDto.getVehicleCode()).build());
            return VehicleDto.builder().vehicleCode(vehicleSaveDto.getVehicleCode()).build();
        }
        else {
            log.info(jwtService.findUsername(token)+" Vehicle not be saved : (VEHICLE CODE) " + vehicleSaveDto.getVehicleCode());
            return null;
        }
    }

    public VehicleDto getVehicleFromId(Long vehicleId){

        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle == null){
            return null;
        }
        else {
            return VehicleDto.builder().vehicleDefect(vehicle.getVehicleDefect()).vehicleCode(vehicle.getCode()).build();
        }

    }

    public Vehicle getVehicleFromCode(String vehicleCode){

        Vehicle vehicle = vehicleRepository.findByCodeAndDeletedFalse(vehicleCode);
        if (vehicle == null){
            return null;
        }
        else {
            return vehicle;
        }

    }

    public void vehicleUpdate(Long id,VehicleDto vehicleDto,String token){
        log.info(jwtService.findUsername(token)+" Send vehicle update request : (VEHICLE CODE) " + id);
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(id);
        if (vehicle != null){
            vehicle.setCode(vehicleDto.getVehicleCode());
            vehicle.setVehicleDefect(vehicleDto.getVehicleDefect());
            log.info("Vehicle Updated : (id) " + id);
            vehicleRepository.save(vehicle);
        }
        log.info("Vehicle not found : (id) " + id);

    }

    public void vehicleDelete(Long vehicleId, String token){
        log.info(jwtService.findUsername(token)+" Send vehicle delete request : (VEHICLE CODE) " + vehicleId);

        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){
            vehicle.setDeleted(true);
            vehicleRepository.save(vehicle);
            return;
        }
        log.error(jwtService.findUsername(token)+" Vehicle Not Found! (id)" + vehicleId );
    }

}
