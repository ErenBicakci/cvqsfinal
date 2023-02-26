package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.entity.*;
import com.toyota.cvqsfinal.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Slf4j
@Service
@RequiredArgsConstructor
public class DefectService {
    private final JwtService jwtService;
    private final ImageRepository imageRepository;
    private final DefectRepository defectRepository;


    public DefectDto defectSave(DefectDto defectDto)throws  Exception{


            byte[] imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());



            Image image = Image.builder()
                    .contentType(defectDto.getImageDto().getType())
                    .data(imageData)
                    .name(defectDto.getName())
                    .build();

            imageRepository.save(image);

            Defect newDefect =
                    Defect.builder()
                            .defectName(defectDto.getName())
                            .image(image)
                            .build();
            defectRepository.save(newDefect);

            return  DefectDto.builder().imageDto(defectDto.getImageDto()).name(defectDto.getName()).build();

    }

    @Transactional
    public boolean defectDelete(Long defectId){
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            defect.setDeleted(true);
            defect.getImage().setDeleted(true);
            imageRepository.save(defect.getImage());
            defectRepository.save(defect);
            return true;
        }
        return false;
    }

    @Transactional
    public DefectDto defectGet(Long defectId){
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .imageDto(ImageDto.builder().name(defect.getImage().getName()).data("").type(defect.getImage().getContentType()).build())
                    .build();
        }
        return null;
    }

    @Transactional
    public ByteArrayResource getImage(Long defectId){
        try {
            final ByteArrayResource inputStream = new ByteArrayResource(
                    defectRepository.getDefectByIdAndDeletedFalse(defectId).getImage().getData()
            );
            return inputStream;
        }
        catch (Exception e){
            return null;
        }

    }

    @Transactional
    public DefectDto updateDefect(Long defectId,DefectDto defectDto){

        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            defect.setDefectName(defect.getDefectName());
            defect.getImage().setName(defectDto.getImageDto().getName());

            byte[] imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

            defect.getImage().setData(imageData);
            defect.getImage().setContentType(defectDto.getImageDto().getType());
            imageRepository.save(defect.getImage());
            defectRepository.save(defect);
        }
        return null;
    }


}
