package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.DefectDto;
import com.toyota.cvqsfinal.dto.GetDefectParameters;
import com.toyota.cvqsfinal.dto.GetVehicleParameters;
import com.toyota.cvqsfinal.dto.ImageDto;
import com.toyota.cvqsfinal.entity.*;
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
     * Hata kaydetme servisi
     *
     * @param defectDto - Hata bilgileri ve resim bilgileri
     * @return DefectDto - Hata bilgileri ve resim bilgileri
     * @throws Exception
     */

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

    /**
     *
     * Hata silme servisi
     *
     * @param defectId - Hata id
     * @return boolean - Hata silme işlemi başarılı ise true, başarısız ise false döner
     */

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

    /**
     *
     * Hata getirme servisi
     *
     * @param defectId - Hata id
     * @return DefectDto - hatanın bilgileri ve resmi
     */

    @Transactional
    public DefectDto defectGet(Long defectId){
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .id(defect.getId())
                    .imageDto(ImageDto.builder().id(defect.getImage().getId()).name(defect.getImage().getName()).data("http://localhost:8080/api/defect/image/"+defect.getId()).type(defect.getImage().getContentType()).build())
                    .build();
        }
        return null;
    }

    /**
     *
     * Hatanın resmini getirme servisi
     *
     * @param defectId - Hata id
     * @return ByteArrayResource - Hatanın resmini byte dizisi olarak döner
     */

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

    /**
     *
     * Hata güncelleme servisi
     *
     * @param defectDto - Güncellenecek hata bilgileri ve resim bilgileri
     * @return DefectDto - Güncellenen hata bilgileri ve resim bilgileri
     */

    @Transactional
    public DefectDto updateDefect(DefectDto defectDto){

        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectDto.getId());
        if (defect != null){
            defect.setDefectName(defectDto.getName());
            defect.getImage().setName(defectDto.getImageDto().getName());


            byte[] imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

            defect.getImage().setData(imageData);
            defect.getImage().setContentType(defectDto.getImageDto().getType());
            imageRepository.save(defect.getImage());
            defectRepository.save(defect);
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .imageDto(ImageDto.builder().name(defect.getImage().getName()).data("http://localhost:8080/api/defect/image/"+defect.getId()).type(defect.getImage().getContentType()).build())
                    .build();
        }
        return null;
    }

    /**
     *
     * Hata listeleme servisi
     *
     * @param getDefectParameters - Sayfalama ve filtreleme bilgileri
     * @return List<DefectDto> - Hata listesi
     */

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
