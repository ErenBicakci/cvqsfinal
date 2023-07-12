package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.GetVehicleParameters;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.exception.GenericException;
import com.toyota.cvqsfinal.exception.VehicleNotFoundException;
import com.toyota.cvqsfinal.log.CustomLogDebug;
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

    private final VehicleDefectService vehicleDefectService;


    /**
     *
     * Vehicle save service
     *
     * @param vehicleSaveDto - VehicleDto (info)
     * @return VehicleDto - VehicleDto (info)
     */
    @CustomLogDebug
    public VehicleDto vehicleSave(VehicleDto vehicleSaveDto){
        if (vehicleRepository.findByCodeAndDeletedFalse(vehicleSaveDto.getVehicleCode()) == null){
            Vehicle vehicle = vehicleRepository.save(Vehicle.builder().code(vehicleSaveDto.getVehicleCode()).modelNo(vehicleSaveDto.getModelNo()).build());
            return VehicleDto.builder().id(vehicle.getId()).vehicleCode(vehicleSaveDto.getVehicleCode()).modelNo(vehicleSaveDto.getModelNo()).build();
        }
        else {
            throw new GenericException("Vehicle code already exists");
        }
    }


    /**
     *
     * Get vehicle from id service
     *
     * @param vehicleId - Vehicle id
     * @return VehicleDto - VehicleDto (info)
     */
    @CustomLogDebug
    @Transactional
    public VehicleDto getVehicleFromId(Long vehicleId){
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle == null){
            throw new VehicleNotFoundException("Vehicle not found");
        }
        else {

            return VehicleDto.builder()
                    .vehicleDefectDtos(vehicle.getVehicleDefect()
                            .stream()
                            .filter(vehicleDefect -> !vehicleDefect.isDeleted())
                            .map(dtoConvert::vehicleDefectToVehicleDefectDto)
                            .collect(Collectors.toList()))

                    .id(vehicle.getId())
                    .vehicleCode(vehicle.getCode())
                    .modelNo(vehicle.getModelNo())
                    .build();
        }
    }

    /**
     *
     * Vehicle update service
     *
     * @param vehicleDto - VehicleDto (info)
     * @return VehicleDto - VehicleDto (info)
     */
    @CustomLogDebug
    public VehicleDto vehicleUpdate(VehicleDto vehicleDto){
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleDto.getId());
        if (vehicle != null){
            vehicle.setCode(vehicleDto.getVehicleCode());
            vehicle.setModelNo(vehicleDto.getModelNo());
            vehicleRepository.save(vehicle);
            return vehicleDto;
        }
        throw new VehicleNotFoundException("Vehicle not found");
    }


    /**
     *
     * Vehicle delete service
     *
     * @param vehicleId - Vehicle id
     * @return boolean - if vehicle deleted true else false
     */

    @CustomLogDebug
    @Transactional
    public boolean vehicleDelete(Long vehicleId){
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){
            vehicle.getVehicleDefect().stream().filter(vehicleDefect -> !vehicleDefect.isDeleted()).forEach(vehicleDefect -> vehicleDefectService.vehicleDefectDel(vehicleDefect.getId()));
            vehicle.setDeleted(true);
            vehicleRepository.save(vehicle);
            return true;
        }
        throw new VehicleNotFoundException("Vehicle not found");
    }


    /**
     *
     * Vehicle list service with pagination and sorting
     *
     * @param getVehicleParameters - GetVehicleParameters (info) - page, pageSize, sortType, vehicleCode, modelNo
     * @return List<VehicleDto> - VehicleDto (info)
     */
    @CustomLogDebug
    @Transactional
    public List<VehicleDto> getVehiclesWithPagination(GetVehicleParameters getVehicleParameters){
        Sort sort;
        if (getVehicleParameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getVehicleParameters.getPage(), getVehicleParameters.getPageSize(), sort);
        return vehicleRepository
                .findAllByCodeLikeAndModelNoLikeAndDeletedFalse("%"+ getVehicleParameters.getVehicleCode()+"%","%"+ getVehicleParameters.getModelNo()+"%",pageable)
                .get()
                .map(dtoConvert::vehicleToVehicleDto)
                .toList();
    }

}
