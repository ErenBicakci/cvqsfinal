package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.VehicleDefect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleDefectRepository extends JpaRepository<VehicleDefect,Long> {
    VehicleDefect getVehicleDefectByIdAndDeletedFalse(Long id);
}
