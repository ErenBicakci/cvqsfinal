package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.entity.*;
import com.toyota.cvqsfinal.repository.DefectLocationRepository;
import com.toyota.cvqsfinal.repository.ImageRepository;
import com.toyota.cvqsfinal.repository.VehicleDefectRepository;
import com.toyota.cvqsfinal.repository.VehicleRepository;
import com.toyota.cvqsfinal.utility.ImageOperations;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Base64;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefectService {
    private final JwtService jwtService;
    private final VehicleRepository vehicleRepository;
    private final DefectLocationRepository defectLocationRepository;
    private final VehicleDefectRepository vehicleDefectRepository;
    private final ImageRepository imageRepository;
    private final ImageOperations imageOperations;


    public DefectDto defectSave(String username, Long vehicleId, DefectDto defectDto)throws  Exception{
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){

            byte[] imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

            defectLocationRepository.saveAll(defectDto.getDefectLocations());

            Image image = Image.builder()
                    .contentType(defectDto.getImageDto().getType())
                    .data(imageData)
                    .name(defectDto.getName())
                    .build();
            image.setData(imageOperations.markImage(image,defectDto.getDefectLocations()).getData());

            imageRepository.save(image);

            VehicleDefect newDefect =
                    VehicleDefect.builder()
                            .defectName(defectDto.getName())
                            .defectLocations(defectDto.getDefectLocations())
                            .image(image)
                            .build();
            vehicleDefectRepository.save(newDefect);



            List<VehicleDefect> vehicleDefects = vehicle.getVehicleDefect();
            vehicleDefects.add(newDefect);
            vehicle.setVehicleDefect(vehicleDefects);
            vehicleRepository.save(vehicle);
            return  DefectDto.builder().imageDto(defectDto.getImageDto()).name(defectDto.getName()).defectLocations(defectDto.getDefectLocations()).build();
        }
        return null;
    }

    @Transactional
    public void defectDelete(Long defectId,String username){

        log.info(jwtService.findUsername(username) + "Send defect delete request : (id) " + defectId);
        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(defectId);
        if (vehicleDefect != null){
            vehicleDefect.setDeleted(true);
            vehicleDefect.getImage().setDeleted(true);
            imageRepository.save(vehicleDefect.getImage());
            vehicleDefectRepository.save(vehicleDefect);
            log.info("Defect not found : (id) " + defectId);
        }
        log.debug("defect delted successfully");
    }

    @Transactional
    public DefectDto defectGet(Long defectId){
        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(defectId);
        if (vehicleDefect != null){
            return DefectDto.builder()
                    .defectLocations(vehicleDefect.getDefectLocations())
                    .name(vehicleDefect.getDefectName())
                    .imageDto(ImageDto.builder().name(vehicleDefect.getImage().getName()).data("").type(vehicleDefect.getImage().getContentType()).build())
                    .build();
        }
        return null;
    }

    @Transactional
    public ByteArrayResource getImage(Long defectId){
        try {
            final ByteArrayResource inputStream = new ByteArrayResource(
                    vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(defectId).getImage().getData()
            );
            return inputStream;
        }
        catch (Exception e){
            return null;
        }

    }

}
