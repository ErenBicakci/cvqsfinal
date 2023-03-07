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
     */

    public DefectDto defectSave(DefectDto defectDto){
        log.debug("defectSave methodu çalıştı");
        byte[] imageData = null;
        try {
             imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

        }
        catch (IllegalArgumentException e){
            log.warn("defectSave methouna gönderilen resim base64 formatında değil");
            return null;
        }

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

            log.debug("defectSave methodu çalıştı");
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
        log.debug("defectDelete methodu çalıştı");
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            defect.setDeleted(true);
            defect.getImage().setDeleted(true);
            imageRepository.save(defect.getImage());
            defectRepository.save(defect);
            return true;
        }
        log.debug("defectDelete üzerinden false döndü");
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
        log.debug("defectGet methodu çalıştı");
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectId);
        if (defect != null){
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .id(defect.getId())
                    .imageDto(ImageDto.builder().id(defect.getImage().getId()).name(defect.getImage().getName()).data("http://localhost:8080/api/defect/image/"+defect.getId()).type(defect.getImage().getContentType()).build())
                    .build();
        }
        log.debug("defectGet üzerinden null döndü");
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
        log.debug("getImage methodu çalıştı");
            final ByteArrayResource inputStream = new ByteArrayResource(defectRepository.getDefectByIdAndDeletedFalse(defectId).getImage().getData());
            log.debug("getImage üzerinden ByteArrayResource döndü");
            return inputStream;

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
        log.debug("updateDefect methodu çalıştı");

        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(defectDto.getId());
        if (defect != null){
            defect.setDefectName(defectDto.getName());
            defect.getImage().setName(defectDto.getImageDto().getName());


            byte[] imageData = null;
            try {
                imageData = Base64.getMimeDecoder().decode(defectDto.getImageDto().getData());

            }
            catch (IllegalArgumentException e){
                log.warn("defectUpdate methouna gönderilen resim base64 formatında değil");
                return null;
            }

            defect.getImage().setData(imageData);
            defect.getImage().setContentType(defectDto.getImageDto().getType());
            imageRepository.save(defect.getImage());
            defectRepository.save(defect);
            log.debug("updateDefect üzerinden DefectDto döndü");
            return DefectDto.builder()
                    .name(defect.getDefectName())
                    .imageDto(ImageDto.builder().name(defect.getImage().getName()).data("http://localhost:8080/api/defect/image/"+defect.getId()).type(defect.getImage().getContentType()).build())
                    .build();
        }
        log.debug("updateDefect üzerinden null döndü");
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
        log.debug("getDefectsWithPagination methodu çalıştı");
        Sort sort;
        if (getDefectParameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(getDefectParameters.getPage(), getDefectParameters.getPageSize(), sort);
        log.debug("getDefectsWithPagination üzerinden List<DefectDto> döndü");
        return defectRepository.getAllByDefectNameLikeAndDeletedFalse("%"+ getDefectParameters.getFilterKeyword()+"%",pageable).get().collect(Collectors.toList()).stream().map(defect -> dtoConvert.defectToDefectDto(defect)).collect(Collectors.toList());
    }


}
