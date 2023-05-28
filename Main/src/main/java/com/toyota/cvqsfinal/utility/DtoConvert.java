package com.toyota.cvqsfinal.utility;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Defect;
import com.toyota.cvqsfinal.entity.Image;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class DtoConvert {


    /**
     *  DefectDto convert to Defect
     * @param defectDto - DefectDto (info and image)
     * @return Defect - Defect (info and image)
     */
    public Defect defectDtoToDefect(DefectDto defectDto){
        Defect defect = new Defect();
        defect.setId(defectDto.getId());

        byte[] imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());


        defect.setImage(Image.builder()
                        .contentType(defectDto.getImageDto().getType())
                        .name(defectDto.getImageDto().getName())
                        .data(imageData)
                        .build());

        defect.setDefectName(defectDto.getName());

        return defect;
    }


    /**
     *  Defect convert to DefectDto
     * @param defect - Defect (info and image)
     * @return DefectDto - DefectDto (info and image)
     */
    public DefectDto defectToDefectDto(Defect defect){
        DefectDto defectDto = new DefectDto();
        defectDto.setId(defect.getId());
        defectDto.setName(defect.getDefectName());
        defectDto.setImageDto(ImageDto.builder()
                .id(defect.getImage().getId())
                .name(defect.getImage().getName())
                .type(defect.getImage().getContentType())
                .data("")
                .build());
        return defectDto;
    }

    /**
     * VehicleDefectDto convert to VehicleDefect
     * @param vehicleDefectDto - VehicleDefectDto (info and image)
     * @return VehicleDefect - VehicleDefect (info and image)
     */
    public VehicleDefect vehicleDefectDtoToVehicleDefect(VehicleDefectDto vehicleDefectDto){

        VehicleDefect vehicleDefect = new VehicleDefect();
        vehicleDefect.setId(vehicleDefectDto.getId());
        vehicleDefect.setDefectLocations(vehicleDefectDto.getDefectLocations());
        vehicleDefect.setDefect(defectDtoToDefect(vehicleDefectDto.getDefect()));
        return vehicleDefect;
    }

    /**
     *  VehicleDefect convert to VehicleDefectDto
     * @param vehicleDefect - VehicleDefect (info and image)
     * @return VehicleDefectDto - VehicleDefectDto (info and image)
     */

    public VehicleDefectDto vehicleDefectToVehicleDefectDto(VehicleDefect vehicleDefect){
        VehicleDefectDto vehicleDefectDto = new VehicleDefectDto();
        vehicleDefectDto.setId(vehicleDefect.getId());
        vehicleDefectDto.setDefectLocations(vehicleDefect.getDefectLocations());
        vehicleDefectDto.setDefect(defectToDefectDto(vehicleDefect.getDefect()));
        return vehicleDefectDto;
    }


    /**
     *  VehicleDto convert to Vehicle
     * @param vehicleDto - VehicleDto (info and image)
     * @return Vehicle - Vehicle (info and image)
     */
    public Vehicle vehicleDtoToVehicle(VehicleDto vehicleDto){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleDto.getId());
        vehicle.setCode(vehicleDto.getVehicleCode());
        vehicle.setModelNo(vehicleDto.getModelNo());
        vehicle.setVehicleDefect(vehicleDto.getVehicleDefectDtos().stream().map(this::vehicleDefectDtoToVehicleDefect).toList());
        return vehicle;
    }

    /**
     *  Vehicle convert to VehicleDto
     * @param vehicle -Vehicle (info and image)
     * @return VehicleDto - VehicleDto (info and image)
     */
    public VehicleDto vehicleToVehicleDto(Vehicle vehicle){
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setVehicleCode(vehicle.getCode());
        vehicleDto.setModelNo(vehicle.getModelNo());
        vehicleDto.setVehicleDefectDtos(vehicle.getVehicleDefect().stream().map(this::vehicleDefectToVehicleDefectDto).toList());
        return vehicleDto;
    }
}
