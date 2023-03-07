package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.GetVehicleParameters;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Vehicle;
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


    /**
     *
     * Araç kaydetme servisi
     *
     * @param vehicleSaveDto - Araç bilgileri
     * @return VehicleDto - kaydedilen araç bilgileri
     */

    public VehicleDto vehicleSave(VehicleDto vehicleSaveDto){
        log.debug("vehicleSave methodu çalıştı");
        if (vehicleRepository.findByCodeAndDeletedFalse(vehicleSaveDto.getVehicleCode()) == null){
            vehicleRepository.save(Vehicle.builder().code(vehicleSaveDto.getVehicleCode()).modelNo(vehicleSaveDto.getModelNo()).build());
            log.debug("vehicleSave methodu VehicleDto döndürdü");
            return VehicleDto.builder().vehicleCode(vehicleSaveDto.getVehicleCode()).modelNo(vehicleSaveDto.getModelNo()).build();
        }
        else {
            log.warn("vehicleSave methodu null döndürdü");
            return null;
        }
    }


    /**
     *
     * Araç getirme servisi
     *
     * @param vehicleId - Araç id
     * @return VehicleDto - Getirilen araç bilgileri
     */

    @Transactional
    public VehicleDto getVehicleFromId(Long vehicleId){

        log.debug("getVehicleFromId methodu çalıştı");
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle == null){
            log.warn("getVehicleFromId methodu null döndürdü");
            return null;
        }
        else {
            log.debug("getVehicleFromId methodu VehicleDto döndürdü");
            return VehicleDto.builder()
                    .vehicleDefectDtos(vehicle.getVehicleDefect().stream().filter(vehicleDefect -> !vehicleDefect.isDeleted()).collect(Collectors.toList()).stream().map(vehicleDefect -> dtoConvert.vehicleDefectToVehicleDefectDto(vehicleDefect)).collect(Collectors.toList()))
                    .id(vehicle.getId())
                    .vehicleCode(vehicle.getCode())
                    .modelNo(vehicle.getModelNo())
                    .build();
        }

    }

    /**
     *
     * Araç güncelleme servisi
     *
     * @param vehicleDto - Araç bilgileri
     * @return VehicleDto - Güncellenen araç bilgileri
     */

    public VehicleDto vehicleUpdate(VehicleDto vehicleDto){
        log.debug("vehicleUpdate methodu çalıştı");
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleDto.getId());
        if (vehicle != null){

            vehicle.setCode(vehicleDto.getVehicleCode());
            vehicle.setModelNo(vehicleDto.getModelNo());
            vehicleRepository.save(vehicle);
            log.debug("vehicleUpdate methodu VehicleDto döndürdü");
            return vehicleDto;
        }
        log.warn("vehicleUpdate methodu null döndürdü");
        return null;
    }


    /**
     *
     * Araç silme servisi
     *
     * @param vehicleId - Araç id
     * @return boolean - Silme işlemi başarılı ise true, başarısız ise false
     */

    public boolean vehicleDelete(Long vehicleId){
        log.debug("vehicleDelete methodu çalıştı");
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
            log.debug("vehicleDelete methodu true döndürdü");
            return true;
        }
        log.warn("vehicleDelete methodu false döndürdü");
        return false;
    }


    /**
     *
     * Araç listeleme servisi
     *
     * @param getVehicleParameters - Getirilecek araç filtreleme, sayfalama ve sıralama bilgileri
     * @return List<VehicleDto> - Getirilen araç bilgileri
     */

    @Transactional
    public List<VehicleDto> getVehiclesWithPagination(GetVehicleParameters getVehicleParameters){
        log.debug("getVehiclesWithPagination methodu çalıştı");
        Sort sort;
        if (getVehicleParameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getVehicleParameters.getPage(), getVehicleParameters.getPageSize(), sort);
        log.debug("getVehiclesWithPagination methodu List<VehicleDto> döndürdü");
        return vehicleRepository.findAllByCodeLikeAndModelNoLikeAndDeletedFalse("%"+ getVehicleParameters.getVehicleCode()+"%","%"+ getVehicleParameters.getModelNo()+"%",pageable).get().collect(Collectors.toList()).stream().map(vehicle -> dtoConvert.vehicleToVehicleDto(vehicle)).collect(Collectors.toList());
    }

}
