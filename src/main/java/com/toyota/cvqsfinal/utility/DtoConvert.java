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
     *  DefectDto nesnesini Defect nesnesine dönüştürür
     * @param defectDto - Hata bilgileri ve resim bilgileri
     * @return Defect - Hata bilgileri ve resim bilgileri
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
     *  Defect nesnesini DefectDto nesnesine dönüştürür
     * @param defect - Hata bilgileri ve resim bilgileri
     * @return DefectDto - Hata bilgileri ve resim bilgileri
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
     *  VehicleDefectDto nesnesini VehicleDefect nesnesine dönüştürür
     * @param vehicleDefectDto - Araç hata bilgileri
     * @return VehicleDefect - Araç hata bilgileri
     */
    public VehicleDefect vehicleDefectDtoToVehicleDefect(VehicleDefectDto vehicleDefectDto){

        VehicleDefect vehicleDefect = new VehicleDefect();
        vehicleDefect.setId(vehicleDefectDto.getId());
        vehicleDefect.setDefectLocations(vehicleDefectDto.getDefectLocations());
        vehicleDefect.setDefect(defectDtoToDefect(vehicleDefectDto.getDefect()));
        return vehicleDefect;
    }

    /**
     *  VehicleDefect nesnesini VehicleDefectDto nesnesine dönüştürür
     * @param vehicleDefect - Araç hata bilgileri
     * @return VehicleDefectDto - Araç hata bilgileri
     */

    public VehicleDefectDto vehicleDefectToVehicleDefectDto(VehicleDefect vehicleDefect){
        VehicleDefectDto vehicleDefectDto = new VehicleDefectDto();
        vehicleDefectDto.setId(vehicleDefect.getId());
        vehicleDefectDto.setDefectLocations(vehicleDefect.getDefectLocations());
        vehicleDefectDto.setDefect(defectToDefectDto(vehicleDefect.getDefect()));
        return vehicleDefectDto;
    }


    /**
     *  VehicleDto nesnesini Vehicle nesnesine dönüştürür
     * @param vehicleDto - Araç bilgileri
     * @return Vehicle - Araç bilgileri
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
     *  Vehicle nesnesini VehicleDto nesnesine dönüştürür
     * @param vehicle - Araç bilgileri
     * @return VehicleDto - Araç bilgileri
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
