package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.GetDefectParameters;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.entity.*;
import com.toyota.cvqsfinal.exception.DefectNotFoundException;
import com.toyota.cvqsfinal.log.CustomLogDebug;
import com.toyota.cvqsfinal.log.CustomLogDebugWithoutParameters;
import com.toyota.cvqsfinal.repository.*;
import com.toyota.cvqsfinal.utility.DtoConvert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
@RequiredArgsConstructor
public class DefectService {
    private final ImageRepository imageRepository;
    private final DefectRepository defectRepository;
    private final DtoConvert dtoConvert;

    /**
     *
     * Defect save service
     *
     * @param defectDto - DefectDto (info and image)
     * @return DefectDto - DefectDto (info and image)
     */

    @CustomLogDebugWithoutParameters
    public boolean defectSave(DefectDto defectDto){
        byte[] imageData = null;
        try {
             imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Image data is not base64 encoded");
        }

            Image image = Image.builder()
                    .contentType(defectDto.getImageDto().getType())
                    .data(imageData)
                    .name(defectDto.getImageDto().getName())
                    .build();

            imageRepository.save(image);

            Defect newDefect =
                    Defect.builder()
                            .defectName(defectDto.getName())
                            .image(image)
                            .build();
            defectRepository.save(newDefect);

            return true;
    }

    /**
     *
     * Defect Delete service
     *
     * @param defectId - Defect id
     * @return boolean - if true deleted
     */
    @CustomLogDebug

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
        throw new DefectNotFoundException("Defect not found");
    }

    /**
     *
     * Defet get service
     *
     * @param defectId - defect id
     * @return DefectDto - defect info and image
     */

    @CustomLogDebug
    @Transactional
    public DefectDto defectGet(Long defectId){
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .id(defect.getId())
                    .imageDto(ImageDto.builder().id(defect.getImage().getId()).name(defect.getImage().getName()).data("").type(defect.getImage().getContentType()).build())
                    .build();
        }
        throw new DefectNotFoundException("Hata bulunamadı");
    }

    /**
     *
     * Defect image get service
     *
     * @param defectId - defect id
     * @return ByteArrayResource - defect image data with ByteArrayResource
     */
    @CustomLogDebugWithoutParameters
    @Transactional
    public ByteArrayResource getImage(Long defectId){
        final ByteArrayResource inputStream = new ByteArrayResource(defectRepository.getDefectByIdAndDeletedFalse(defectId).getImage().getData());
        return inputStream;
    }

    /**
     *
     * Defect update service
     *
     * @param defectDto - Updated defect info and image data
     * @return DefectDto - Updated defect info and image data
     */
    @CustomLogDebugWithoutParameters
    @Transactional
    public DefectDto updateDefect(DefectDto defectDto){

        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectDto.getId());
        if (defect != null){
            defect.setDefectName(defectDto.getName());
            defect.getImage().setName(defectDto.getImageDto().getName());

            byte[] imageData = null;
            try {
                imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

            }
            catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Image data is not base64 encoded");
            }

            defect.getImage().setData(imageData);
            defect.getImage().setContentType(defectDto.getImageDto().getType());
            imageRepository.save(defect.getImage());
            defectRepository.save(defect);
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .imageDto(ImageDto.builder().name(defect.getImage().getName()).data("").type(defect.getImage().getContentType()).build())
                    .build();
        }
        throw new DefectNotFoundException("Defect not found");
    }

    /**
     *
     * Defect list get service with pagination and filter
     *
     * @param getDefectParameters - GetDefectParameters (page, pageSize, sortType, filterKeyword)
     * @return List<DefectDto> - Defect list with pagination, filter and sort
     */
    @CustomLogDebug
    @Transactional
    public List<DefectDto> getDefectsWithPagination(GetDefectParameters getDefectParameters){
        Sort sort;
        if (getDefectParameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getDefectParameters.getPage(), getDefectParameters.getPageSize(), sort);
        return defectRepository.getAllByDefectNameLikeAndDeletedFalse("%"+ getDefectParameters.getFilterKeyword()+"%",pageable).get().collect(Collectors.toList()).stream().map(defect -> dtoConvert.defectToDefectDto(defect)).collect(Collectors.toList());
    }

}
