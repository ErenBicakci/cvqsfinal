package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.GetVehicleDefectParameters;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.entity.*;
import com.toyota.cvqsfinal.exception.DefectNotFoundException;
import com.toyota.cvqsfinal.exception.GenericException;
import com.toyota.cvqsfinal.exception.VehicleDefectNotFoundException;
import com.toyota.cvqsfinal.exception.VehicleNotFoundException;
import com.toyota.cvqsfinal.log.CustomLogDebug;
import com.toyota.cvqsfinal.log.CustomLogDebugWithoutParameters;
import com.toyota.cvqsfinal.repository.*;
import com.toyota.cvqsfinal.utility.DtoConvert;
import com.toyota.cvqsfinal.utility.ImageOperations;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class VehicleDefectService {

    public final ImageOperations imageOperations;

    private final DefectRepository defectRepository;
    private final DefectLocationRepository defectLocationRepository;
    private final VehicleDefectRepository vehicleDefectRepository;

    private final VehicleRepository vehicleRepository;

    private final DtoConvert dtoConvert;

    public VehicleDefectService(ImageOperations imageOperations, DefectRepository defectRepository, DefectLocationRepository defectLocationRepository, VehicleDefectRepository vehicleDefectRepository, VehicleRepository vehicleRepository, DtoConvert dtoConvert) {
        this.imageOperations = imageOperations;
        this.defectRepository = defectRepository;
        this.defectLocationRepository = defectLocationRepository;
        this.vehicleDefectRepository = vehicleDefectRepository;
        this.vehicleRepository = vehicleRepository;
        this.dtoConvert = dtoConvert;
    }

    /**
     *
     * VehicleDefect save service
     *
     * @param vehicleDefectDto - VehicleDefectDto (info and image)
     * @return VehicleDefectDto - VehicleDefectDto (info and image)
     */
    @CustomLogDebug
    @Transactional
    public VehicleDefectDto vehicleDefectSave(VehicleDefectDto vehicleDefectDto,Long vehicleId){
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){

            List<DefectLocation> defectLocations = vehicleDefectDto.getDefectLocations();
            defectLocationRepository.saveAll(defectLocations);
            Defect defect = defectRepository.getDefectByIdAndDeletedFalse(vehicleDefectDto.getDefect().getId());

            if (defect == null){
                throw new DefectNotFoundException("Defect not found");
            }
            VehicleDefect newDefect =
                    VehicleDefect.builder()
                            .defectLocations(defectLocations)
                            .defect(defect)
                            .vehicle(vehicle)
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
        throw new VehicleDefectNotFoundException("Vehicle not found");
    }

    /**
     *
     * VehicleDefect delete service
     *
     * @param id - VehicleDefect id
     * @return boolean - if deleted true
     */
    @CustomLogDebug
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
        throw new VehicleDefectNotFoundException("Vehicle Defect not found");
    }


    /**
     *
     * VehicleDefect update service
     *
     * @param vehicleDefectDto - VehicleDefectDto (info and image)
     * @return VehicleDefectDto - VehicleDefectDto (info and image)
     */
    @CustomLogDebug
    @Transactional
    public VehicleDefectDto vehicleDefectUpdate(VehicleDefectDto vehicleDefectDto) {


        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(vehicleDefectDto.getId());
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(vehicleDefectDto.getDefect().getId());

        if (vehicleDefect == null ){
            throw new VehicleDefectNotFoundException("Vehicle Defect not found");
        }
        if (defect == null ){
            throw new DefectNotFoundException("Defect not found");
        }

        vehicleDefect.setDefect(defect);

        vehicleDefect.getDefectLocations().forEach(defectLocation -> {
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


    /**
     *
     * VehicleDefect get service
     *
     * @param id - VehicleDefect id
     * @return VehicleDefectDto - VehicleDefectDto (info and image)
     */
    @CustomLogDebug
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
        throw new VehicleDefectNotFoundException("Vehicle Defect not found");
    }



    /**
     *
     * Get vehicle defect image
     *
     * @param vehicleDefecetId - VehicleDefect id
     * @return ByteArrayResource - VehicleDefect image data
     */
    @CustomLogDebugWithoutParameters
    @Transactional
    public ByteArrayResource getImage(Long vehicleDefecetId){
        try {
            VehicleDefect vehicleDefect =  vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(vehicleDefecetId);
            byte[] imageData = vehicleDefect.getDefect().getImage().getData();
            return new ByteArrayResource(imageOperations.markImage(imageData, vehicleDefect.getDefectLocations().stream().filter(defectLocation -> !defectLocation.isDeleted()).collect(Collectors.toList())));
        }
        catch (Exception e){
            throw new GenericException("Error while getting image");
        }

    }


    /**
     *
     * VehicleDefect list service with pagination and sorting
     *
     * @param getVehicleDefectParameters - GetVehicleDefectParameters (page, pageSize, sortType, vehicleId)
     * @return List<VehicleDefectDto> - VehicleDefectDto list (info and image)
     */
    @CustomLogDebug
    @Transactional
    public List<VehicleDefectDto> getVehicleDefectsFromVehicleWithPagination(GetVehicleDefectParameters getVehicleDefectParameters) {


        Sort sort;
        if (getVehicleDefectParameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getVehicleDefectParameters.getPage(), getVehicleDefectParameters.getPageSize(), sort);

        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(getVehicleDefectParameters.getVehicleId());

        if (vehicle == null)
            throw new VehicleNotFoundException("Vehicle not found");
        return vehicleDefectRepository.findAllByVehicleAndDeletedFalse(vehicle,pageable).stream().filter(vehicleDefect -> vehicleDefect.getDefect().getDefectName().indexOf(getVehicleDefectParameters.getFilterKeyword()) != -1).map(vehicleDefecet -> dtoConvert.vehicleDefectToVehicleDefectDto(vehicleDefecet)).collect(Collectors.toList());
    }



    @CustomLogDebug
    @Transactional
    public List<VehicleDefectDto> getVehicleDefectsWithPagination(GetVehicleDefectParameters getVehicleDefectParameters) {


        Sort sort;
        if (getVehicleDefectParameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getVehicleDefectParameters.getPage(), getVehicleDefectParameters.getPageSize(), sort);

        return vehicleDefectRepository.findAllVehicleDefectByDeletedFalse(pageable).stream().map(vehicleDefecet -> dtoConvert.vehicleDefectToVehicleDefectDto(vehicleDefecet)).collect(Collectors.toList());
    }




}
