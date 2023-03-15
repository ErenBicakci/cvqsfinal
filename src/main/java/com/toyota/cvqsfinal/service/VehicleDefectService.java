package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.GetVehicleDefectParameters;
import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.*;
import com.toyota.cvqsfinal.entity.Image;
import com.toyota.cvqsfinal.exception.DefectNotFoundException;
import com.toyota.cvqsfinal.exception.GenericException;
import com.toyota.cvqsfinal.exception.VehicleDefectNotFoundException;
import com.toyota.cvqsfinal.exception.VehicleNotFoundException;
import com.toyota.cvqsfinal.repository.*;
import com.toyota.cvqsfinal.utility.DtoConvert;
import com.toyota.cvqsfinal.utility.ImageOperations;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleDefectService {

    public final ImageOperations imageOperations;

    private final DefectRepository defectRepository;
    private final DefectLocationRepository defectLocationRepository;
    private final VehicleDefectRepository vehicleDefectRepository;

    private final VehicleRepository vehicleRepository;

    private final DtoConvert dtoConvert;

    /**
     *
     * Araç hata kaydetme servisi
     *
     * @param vehicleId - Araç id
     * @param vehicleDefectDto - Araç hata bilgileri
     * @return VehicleDefectDto - Araç hata bilgileri
     */

    @Transactional
    public VehicleDefectDto vehicleDefectSave(Long vehicleId, VehicleDefectDto vehicleDefectDto){
        log.debug("vehicleDefectSave methodu çalıştı");
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId);
        if (vehicle != null){

            List<DefectLocation> defectLocations = vehicleDefectDto.getDefectLocations();

            defectLocationRepository.saveAll(defectLocations);

            Defect defect = defectRepository.getDefectByIdAndDeletedFalse(vehicleDefectDto.getDefect().getId());

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
            log.debug("vehicleDefectSave VehicleDefectDto döndü");
            return VehicleDefectDto.builder()
                    .defect(dtoConvert.defectToDefectDto(defect))
                    .defectLocations(defectLocations)
                    .build();
        }
        log.warn("vehicleDefectSave methodu null döndü");
        throw new VehicleDefectNotFoundException("Araç bulunamadı");
    }

    /**
     *
     * Araç hata silme servisi
     *
     * @param id - Araç hata id
     * @return boolean - Araç hata silme işlemi başarılı ise true, başarısız ise false döner
     */

    @Transactional
    public boolean vehicleDefectDel(Long id){
        log.debug("vehicleDefectDel methodu çalıştı");

        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(id);
        if (vehicleDefect != null){
            vehicleDefect.setDeleted(true);
            vehicleDefect.getDefectLocations().forEach(defectLocation -> {
                defectLocation.setDeleted(true);
                defectLocationRepository.save(defectLocation);
            });
            vehicleDefectRepository.save(vehicleDefect);
            log.debug("vehicleDefectDel methodu true döndü");
            return true;
        }
        log.warn("vehicleDefectDel methodu false döndü");
        throw new VehicleDefectNotFoundException("Araç bulunamadı");
    }


    /**
     *
     * Araç hata güncelleme servisi
     *
     * @param vehicleDefectDto - Güncellenecek araç hata bilgileri
     * @return VehicleDefectDto - Güncellenen araç hata bilgileri
     */

    @Transactional
    public VehicleDefectDto vehicleDefectUpdate(VehicleDefectDto vehicleDefectDto) {

        log.debug("vehicleDefectUpdate methodu çalıştı");

        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(vehicleDefectDto.getId());
        Defect defect = defectRepository.getDefectByIdAndDeletedFalse(vehicleDefectDto.getDefect().getId());

        if (vehicleDefect == null ){
            log.warn("vehicleDefectUpdate methodu null döndü");
            throw new VehicleDefectNotFoundException("Araç Hatası bulunamadı");
        }
        if (defect == null ){
            log.warn("vehicleDefectUpdate methodu null döndü");
            throw new DefectNotFoundException("Hata bulunamadı");
        }

        vehicleDefect.setDefect(defect);

        vehicleDefect.getDefectLocations().stream().forEach(defectLocation -> {
            defectLocation.setDeleted(true);
            defectLocationRepository.save(defectLocation);
        });


        defectLocationRepository.saveAll(vehicleDefectDto.getDefectLocations());

        List<DefectLocation> defectLocationsNew = vehicleDefect.getDefectLocations();
        defectLocationsNew.addAll(vehicleDefectDto.getDefectLocations());

        vehicleDefect.setDefectLocations(defectLocationsNew);

        vehicleDefectRepository.save(vehicleDefect);
        log.debug("vehicleDefectUpdate methodu VehicleDefectDto döndü");
        return VehicleDefectDto.builder().defect(dtoConvert.defectToDefectDto(defect)).defectLocations(vehicleDefectDto.getDefectLocations()).build();
    }


    /**
     *
     * Araç hata getirme servisi
     *
     * @param id - Araç hata id
     * @return VehicleDefectDto - Araç hata bilgileri
     */

    @Transactional
    public VehicleDefectDto vehicleDefectGet(Long id) {
        log.debug("vehicleDefectGet methodu çalıştı");
        VehicleDefect vehicleDefect = vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(id);
        if (vehicleDefect != null){
            log.debug("vehicleDefectGet methodu VehicleDefectDto döndü");
            return VehicleDefectDto.builder()
                    .id(vehicleDefect.getId())
                    .defect(dtoConvert.defectToDefectDto(vehicleDefect.getDefect()))
                    .defectLocations(vehicleDefect.getDefectLocations().stream().filter(defectLocation -> !defectLocation.isDeleted()).collect(Collectors.toList()))
                    .build();
        }
        log.warn("vehicleDefectGet methodu null döndü");
        throw new VehicleDefectNotFoundException("Araç bulunamadı");
    }



    /**
     *
     * Araç hata resim getirme servisi
     *
     * @param vehicleDefecetId - Araç hata id
     * @return ByteArrayResource - Araç hata resmi byte dizisi
     */

    @Transactional
    public ByteArrayResource getImage(Long vehicleDefecetId){
        try {
            VehicleDefect vehicleDefect =  vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(vehicleDefecetId);
            byte[] imageData = vehicleDefect.getDefect().getImage().getData();
            ByteArrayResource inputStream2 = new ByteArrayResource(imageOperations.markImage(imageData, vehicleDefect.getDefectLocations().stream().filter(defectLocation -> !defectLocation.isDeleted()).collect(Collectors.toList())));
            return inputStream2;
        }
        catch (Exception e){
            throw new GenericException("Resim getirilirken hata oluştu");
        }

    }


    /**
     *
     * Araç hata listeleme servisi
     *
     * @param getVehicleDefectParameters - Araç hata getirme parametreleri
     * @return List<VehicleDefectDto> - Araç hata bilgileri
     */

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

        Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(getVehicleDefectParameters.getVehicleId());
        if (vehicle == null)
            throw new VehicleNotFoundException("Araç bulunamadı");

        return vehicleDefectRepository.findAllByVehicleAndDeletedFalse(vehicle,pageable).stream().filter(vehicleDefect -> vehicleDefect.getDefect().getDefectName().indexOf(getVehicleDefectParameters.getFilterKeyword()) != -1).map(vehicleDefecet -> dtoConvert.vehicleDefectToVehicleDefectDto(vehicleDefecet)).collect(Collectors.toList());
    }
}
