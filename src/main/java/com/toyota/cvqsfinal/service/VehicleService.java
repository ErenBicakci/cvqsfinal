package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.GetVehiclePageable;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Defect;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import com.toyota.cvqsfinal.repository.VehicleDefectRepository;
import com.toyota.cvqsfinal.repository.VehicleRepository;
import com.toyota.cvqsfinal.utility.DtoConvert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final VehicleDefectRepository vehicleDefectRepository;

    private final DtoConvert dtoConvert;

    public VehicleDto vehicleSave(VehicleDto vehicleSaveDto){
        if (vehicleRepository.findByCodeAndDeletedFalse(vehicleSaveDto.getVehicleCode()) == null){
            vehicleRepository.save(Vehicle.builder().code(vehicleSaveDto.getVehicleCode()).modelNo(vehicleSaveDto.getModelNo()).build());
            return VehicleDto.builder().vehicleCode(vehicleSaveDto.getVehicleCode()).modelNo(vehicleSaveDto.getModelNo()).build();
        }
        else {
            return null;
        }
    }

    @Transactional
    public VehicleDto getVehicleFromId(Long vehicleId){

        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle == null){
            return null;
        }
        else {
            return VehicleDto.builder()
                    .vehicleDefectDtos(vehicle.getVehicleDefect().stream().filter(vehicleDefect -> !vehicleDefect.isDeleted()).collect(Collectors.toList()).stream().map(vehicleDefect -> dtoConvert.vehicleDefectToVehicleDefectDto(vehicleDefect)).collect(Collectors.toList()))
                    .id(vehicle.getId())
                    .vehicleCode(vehicle.getCode())
                    .modelNo(vehicle.getModelNo())
                    .build();
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

    public VehicleDto vehicleUpdate(VehicleDto vehicleDto){
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleDto.getId());
        if (vehicle != null){
            vehicle.setCode(vehicleDto.getVehicleCode());
            vehicle.setModelNo(vehicle.getModelNo());
            vehicle.setId(vehicle.getId());
            vehicleRepository.save(vehicle);
            return vehicleDto;
        }
        return null;
    }

    public boolean vehicleDelete(Long vehicleId){

        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){
            vehicle.setDeleted(true);
            vehicle.getVehicleDefect().forEach(
                    vehicleDefect -> {
                        vehicleDefect.setDeleted(true);
                        vehicleDefectRepository.save(vehicleDefect);
                    }
            );
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }

    public List<Vehicle> getVehiclesWithPagination(GetVehiclePageable getVehiclePageable){

        Sort sort;
        if (getVehiclePageable.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getVehiclePageable.getPage(), getVehiclePageable.getPageSize(), sort);
        return vehicleRepository.findAllByCodeLikeAndModelNoLikeAndDeletedFalse("%"+getVehiclePageable.getVehicleCode()+"%","%"+getVehiclePageable.getModelNo()+"%",pageable).get().collect(Collectors.toList());
    }

}
