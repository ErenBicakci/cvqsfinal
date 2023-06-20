package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.*;
import com.toyota.cvqsfinal.entity.*;
import com.toyota.cvqsfinal.repository.*;
import com.toyota.cvqsfinal.utility.DtoConvert;
import com.toyota.cvqsfinal.utility.ImageOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleDefectServiceTest {

    @Mock
    private VehicleDefectRepository vehicleDefectRepository;

    @Mock
    private DefectLocationRepository defectLocationRepository;

    @Spy
    private ImageOperations imageOperations;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Spy
    private DtoConvert dtoConvert;

    private VehicleDefectService vehicleDefectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleDefectService = new VehicleDefectService(imageOperations, defectRepository, defectLocationRepository, vehicleDefectRepository, vehicleRepository, dtoConvert);
    }

    @Test
    void testVehicleDefectSaveTest() {
        List<DefectLocation> defectLocationList = new ArrayList<>();
        DefectLocation defectLocation = DefectLocation.builder().id(1L).coordX("15").coordY("30").build();
        defectLocationList.add(defectLocation);

        VehicleDefectDto vehicleDefectDto = VehicleDefectDto
                .builder().id(1L)
                .defect(DefectDto.builder().id(1L).name("defectTest")
                        .imageDto(ImageDto.builder().type("JPG").build()).build())
                .defectLocations(defectLocationList)
                .build();

        List<VehicleDefect> vehicleDefectList = new ArrayList<>();

        Vehicle vehicle = Vehicle.builder().id(1L).code("15 xyz").deleted(false).modelNo("230").vehicleDefect(vehicleDefectList).build();

        Mockito.when(vehicleRepository.findByIdAndDeletedFalse(1L)).thenReturn(vehicle);
        Mockito.when(defectRepository.getDefectByIdAndDeletedFalse(1L)).thenReturn(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build());
        vehicleDefectService.vehicleDefectSave(vehicleDefectDto,1L);

        Mockito.verify(vehicleDefectRepository, Mockito.times(1)).save(Mockito.any(VehicleDefect.class));
        Mockito.verify(defectLocationRepository, Mockito.times(1)).saveAll(Mockito.anyList());

    }

    @Test
    void testVehicleDefectDel() {
        List<DefectLocation> defectLocationList = new ArrayList<>();
        DefectLocation defectLocation = DefectLocation.builder().id(1L).coordX("15").coordY("30").build();
        defectLocationList.add(defectLocation);

        VehicleDefect vehicleDefect = VehicleDefect.builder().deleted(false).id(1L).defect(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build()).defectLocations(defectLocationList).build();
        Mockito.when(vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(1L)).thenReturn(vehicleDefect);
        boolean bool = vehicleDefectService.vehicleDefectDel(1L);
        Mockito.verify(vehicleDefectRepository, Mockito.times(1)).save(vehicleDefect);
        assertTrue(vehicleDefect.isDeleted());
        assertTrue( bool);

    }

    @Test
    void testVehicleDefectUpdate() {
        List<DefectLocation> defectLocationList = new ArrayList<>();
        DefectLocation defectLocation = DefectLocation.builder().id(1L).coordX("15").coordY("30").build();
        defectLocationList.add(defectLocation);

        List<DefectLocation> defectLocationList2 = new ArrayList<>();
        DefectLocation defectLocation2 = DefectLocation.builder().id(1L).coordX("15").coordY("30").build();
        DefectLocation defectLocation3 = DefectLocation.builder().id(2L).coordX("15").coordY("30").build();
        defectLocationList2.add(defectLocation2);
        defectLocationList2.add(defectLocation3);


        VehicleDefect vehicleDefect = VehicleDefect.builder().deleted(false).id(1L).defect(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build()).defectLocations(defectLocationList).build();

        VehicleDefectDto vehicleDefectDto = VehicleDefectDto
                .builder().id(1L)
                .defect(DefectDto.builder().id(1L).name("defectTest")
                        .imageDto(ImageDto.builder().type("JPG").build()).build())
                .defectLocations(defectLocationList2)
                .build();

        Mockito.when(vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(1L)).thenReturn(vehicleDefect);
        Mockito.when(defectRepository.getDefectByIdAndDeletedFalse(1L)).thenReturn(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build());
        vehicleDefectService.vehicleDefectUpdate(vehicleDefectDto);


        Mockito.verify(vehicleDefectRepository, Mockito.times(1)).save(Mockito.any(VehicleDefect.class));
        Mockito.verify(defectLocationRepository, Mockito.times(1)).saveAll(defectLocationList2);

    }

    @Test
    void testVehicleDefectGet() {
        List<DefectLocation> defectLocationList = new ArrayList<>();
        DefectLocation defectLocation = DefectLocation.builder().id(1L).coordX("15").coordY("30").build();
        defectLocationList.add(defectLocation);
        VehicleDefect vehicleDefect = VehicleDefect.builder().deleted(false).id(1L).defect(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build()).defectLocations(defectLocationList).build();


        Mockito.when(vehicleDefectRepository.getVehicleDefectByIdAndDeletedFalse(1L)).thenReturn(vehicleDefect);
        VehicleDefectDto vehicleDefectDto = vehicleDefectService.vehicleDefectGet(1L);
        assertEquals(vehicleDefect.getId(), vehicleDefectDto.getId());
        assertEquals(vehicleDefect.getDefect().getId(), vehicleDefectDto.getDefect().getId());
        assertEquals(vehicleDefect.getDefect().getDefectName(), vehicleDefectDto.getDefect().getName());
        assertEquals(vehicleDefect.getDefectLocations().get(0).getId(), vehicleDefectDto.getDefectLocations().get(0).getId());
    }

    @Test
    void testGetVehicleDefectsFromVehicleWithPagination() {
        GetVehicleDefectParameters parameters = GetVehicleDefectParameters.builder().vehicleId(1L).page(0).pageSize(10).filterKeyword("").sortType("ASC").build();

        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

        Vehicle vehicle = Vehicle.builder().id(1L).code("15 xyz").deleted(false).modelNo("230").build();

        VehicleDefect vehicleDefect = VehicleDefect.builder().deleted(false).id(1L).defect(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build()).build();

        List<VehicleDefect> vehicleDefectList = new ArrayList<>();
        vehicleDefectList.add(vehicleDefect);

        Mockito.when(vehicleDefectRepository.findAllByVehicleAndDeletedFalse(  vehicle,pageable)).thenReturn(new PageImpl<>(vehicleDefectList));
        Mockito.when(vehicleRepository.findByIdAndDeletedFalse(1L)).thenReturn(vehicle);
        List<VehicleDefectDto> vehicleDefectDtos = vehicleDefectService.getVehicleDefectsFromVehicleWithPagination(parameters);

        Mockito.verify(vehicleDefectRepository, Mockito.times(1)).findAllByVehicleAndDeletedFalse(  vehicle,pageable);

        assertEquals(1, vehicleDefectDtos.size());
        assertEquals(vehicleDefect.getId(), vehicleDefectDtos.get(0).getId());
        assertEquals(vehicleDefect.getDefect().getId(), vehicleDefectDtos.get(0).getDefect().getId());
        assertEquals(vehicleDefect.getDefect().getDefectName(), vehicleDefectDtos.get(0).getDefect().getName());

    }

    @Test
    void testGetVehicleDefectsWithPagination() {
        GetVehicleDefectParameters parameters = GetVehicleDefectParameters.builder().page(0).pageSize(10).filterKeyword("").sortType("ASC").build();
        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

        VehicleDefect vehicleDefect = VehicleDefect.builder().deleted(false).id(1L).defect(Defect.builder().id(1L).defectName("defectTest").image(Image.builder().build()).build()).build();
        List<VehicleDefect> vehicleDefectList = new ArrayList<>();
        vehicleDefectList.add(vehicleDefect);


        Mockito.when(vehicleDefectRepository.findAllVehicleDefectByDeletedFalse(pageable)).thenReturn(new PageImpl<>(vehicleDefectList));
        List<VehicleDefectDto> vehicleDefectDtos = vehicleDefectService.getVehicleDefectsWithPagination(parameters);

        Mockito.verify(vehicleDefectRepository, Mockito.times(1)).findAllVehicleDefectByDeletedFalse(  pageable);

        assertEquals(vehicleDefect.getId(), vehicleDefectDtos.get(0).getId());
        assertEquals(vehicleDefect.getDefect().getId(), vehicleDefectDtos.get(0).getDefect().getId());
        assertEquals(vehicleDefect.getDefect().getDefectName(), vehicleDefectDtos.get(0).getDefect().getName());
    }



}