package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.VehicleDefectDto;
import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import com.toyota.cvqsfinal.repository.DefectLocationRepository;
import com.toyota.cvqsfinal.repository.ImageRepository;
import com.toyota.cvqsfinal.repository.VehicleDefectRepository;
import com.toyota.cvqsfinal.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleDefectService {

    private final VehicleService vehicleService;
    private final DefectLocationRepository defectLocationRepository;
    private final VehicleDefectRepository vehicleDefectRepository;
    private final ImageRepository imageRepository;

    private final VehicleRepository vehicleRepository;


}
