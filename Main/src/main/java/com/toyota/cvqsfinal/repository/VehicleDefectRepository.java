package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.Vehicle;
import com.toyota.cvqsfinal.entity.VehicleDefect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleDefectRepository extends JpaRepository<VehicleDefect,Long> {
    VehicleDefect getVehicleDefectByIdAndDeletedFalse(Long id);

    Page<VehicleDefect> findAllByVehicleAndDeletedFalse(Vehicle vehicle, Pageable pageable);
    Page<VehicleDefect> findAllVehicleDefectByDeletedFalse(Pageable pageable);

    List<VehicleDefect> findVehicleDefectsByDefectId(Long vehicleId);
}
