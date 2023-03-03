package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Defect;
import com.toyota.cvqsfinal.entity.DefectLocation;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import com.toyota.cvqsfinal.repository.*;
import com.toyota.cvqsfinal.utility.DtoConvert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleDefectService {

    private final VehicleService vehicleService;
    private final DefectRepository defectRepository;
    private final DefectLocationRepository defectLocationRepository;
    private final VehicleDefectRepository vehicleDefectRepository;
    private final ImageRepository imageRepository;

    private final VehicleRepository vehicleRepository;

    private final DtoConvert dtoConvert;

    @Transactional
    public VehicleDefectDto vehicleDefectSave(Long vehicleId, VehicleDefectDto vehicleDefectDto)throws  Exception{
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){

            List<DefectLocation> defectLocations = vehicleDefectDto.getDefectLocations();

            defectLocationRepository.saveAll(defectLocations);

            Defect defect = defectRepository.getDefectByIdAndDeletedFalse(vehicleDefectDto.getDefect().getId());

            VehicleDefect newDefect =
                    VehicleDefect.builder()
                            .defectLocations(defectLocations)
                            .defect(defect)
                            .build();
            vehicleDefectRepository.save(newDefect);


            List<VehicleDefect> vehicleDefects = vehicle.getVehicleDefect();
            vehicleDefects.add(newDefect);
            vehicle.setVehicleDefect(vehicleDefects);
            vehicleRepository.save(vehicle);
            return VehicleDefectDto.builder()
                    .defect(dtoConvert.defectToDefectDto(defect))
                    .defectLocations(defectLocations)
                    .build();
        }
        return null;
    }

    @Transactional
    public boolean vehicleDefectDel(Long id){


        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(id);
        if (vehicleDefect != null){
            vehicleDefect.setDeleted(true);
            vehicleDefect.getDefectLocations().forEach(defectLocation -> {
                defectLocation.setDeleted(true);
                defectLocationRepository.save(defectLocation);
            });
            vehicleDefectRepository.save(vehicleDefect);
            return true;
        }
        return false;
    }

    @Transactional
    public VehicleDefectDto vehicleDefectUpdate(VehicleDefectDto vehicleDefectDto) {

        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(vehicleDefectDto.getId());
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(vehicleDefectDto.getDefect().getId());
        vehicleDefect.setDefect(defect);

        vehicleDefect.getDefectLocations().stream().forEach(defectLocation -> {
            defectLocation.setDeleted(true);
            defectLocationRepository.save(defectLocation);
        });


        defectLocationRepository.saveAll(vehicleDefectDto.getDefectLocations());

        List<DefectLocation> defectLocationsNew = vehicleDefect.getDefectLocations();
        defectLocationsNew.addAll(vehicleDefectDto.getDefectLocations());

        vehicleDefect.setDefectLocations(defectLocationsNew);

        vehicleDefectRepository.save(vehicleDefect);

        return VehicleDefectDto.builder().defect(dtoConvert.defectToDefectDto(defect)).defectLocations(vehicleDefectDto.getDefectLocations()).build();
    }


    @Transactional
    public VehicleDefectDto vehicleDefectGet(Long id) {
        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(id);
        if (vehicleDefect != null){
            return VehicleDefectDto.builder()
                    .id(vehicleDefect.getId())
                    .defect(dtoConvert.defectToDefectDto(vehicleDefect.getDefect()))
                    .defectLocations(vehicleDefect.getDefectLocations().stream().filter(defectLocation -> !defectLocation.isDeleted()).collect(Collectors.toList()))
                    .build();
        }
        return null;
    }
}