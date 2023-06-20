package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.GetVehicleParameters;
import com.toyota.cvqsfinal.dto.VehicleDto;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import com.toyota.cvqsfinal.repository.VehicleDefectRepository;
import com.toyota.cvqsfinal.repository.VehicleRepository;
import com.toyota.cvqsfinal.utility.DtoConvert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceTest {


    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleDefectRepository vehicleDefectRepository;

    @Spy
    private DtoConvert dtoConvert;

    private VehicleService vehicleService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleService = new VehicleService(vehicleRepository, vehicleDefectRepository, dtoConvert);
    }

    @Test
    void testVehicleSave() {
        Vehicle vehicle = Vehicle.builder().code("15 xyz").modelNo("230").build();
        VehicleDto vehicleDto = VehicleDto.builder().vehicleCode("15 xyz").modelNo("230").build();
        Mockito.when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        vehicleService.vehicleSave(vehicleDto);
        Mockito.verify(vehicleRepository, Mockito.times(1)).save(vehicle);

    }

    @Test
    void testGetVehicleFromId() {
        Vehicle vehicle = Vehicle.builder().id(1L).code("15 xyz").modelNo("230").build();
        Mockito.when(vehicleRepository.findByIdAndDeletedFalse(1L)).thenReturn(vehicle);
        List<VehicleDefect> vehicleDefectList = new ArrayList<>();
        vehicle.setVehicleDefect(vehicleDefectList);
        vehicleService.getVehicleFromId(1L);
        Mockito.verify(vehicleRepository, Mockito.times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void testVehicleUpdate() {
        VehicleDto vehicleDto = VehicleDto.builder().id(1L).vehicleCode("15 xyz5").modelNo("z31231231").build();

        Vehicle vehicle2 = Vehicle.builder().id(1L).code("16 x3sz").modelNo("312321").build();


        Mockito.when(vehicleRepository.findByIdAndDeletedFalse(1L)).thenReturn(vehicle2);
        vehicleService.vehicleUpdate(vehicleDto);
        Mockito.verify(vehicleRepository, Mockito.times(1)).findByIdAndDeletedFalse(1L);
        Vehicle vehicle = Vehicle.builder().id(1L).code("15 xyz5").modelNo("z31231231").build();
        Mockito.verify(vehicleRepository, Mockito.times(1)).save(vehicle);
    }

    @Test
    void testVehicleDelete() {
        Vehicle vehicle = Vehicle.builder().id(1L).code("15 xyz").vehicleDefect(new ArrayList<>()).modelNo("230").build();
        Mockito.when(vehicleRepository.findByIdAndDeletedFalse(1L)).thenReturn(vehicle);
        assertTrue( vehicleService.vehicleDelete(1L));
        Mockito.verify(vehicleRepository, Mockito.times(1)).findByIdAndDeletedFalse(1L);
        Mockito.verify(vehicleRepository, Mockito.times(1)).save(vehicle);
        assertTrue( vehicle.isDeleted());
    }

    @Test
    void testGetVehiclesWithPagination() {
        GetVehicleParameters parameters = GetVehicleParameters.builder().page(0).pageSize(10).sortType("ASC").vehicleCode("").modelNo("").build();

        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(Vehicle.builder().id(1L).code("15 xyz").modelNo("230").build());
        vehicleList.add(Vehicle.builder().id(2L).code("15 xyz").modelNo("230").build());

        Mockito.when(vehicleRepository.findAllByCodeLikeAndModelNoLikeAndDeletedFalse(  "%%","%%",pageable)).thenReturn(null);
        try {
            vehicleService.getVehiclesWithPagination(parameters);
        }
        catch (Exception e){

        }
        Mockito.verify(vehicleRepository, Mockito.times(1)).findAllByCodeLikeAndModelNoLikeAndDeletedFalse(  "%%","%%",pageable);
    }
}