package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    Vehicle findByCodeAndDeletedFalse(String code);
    Vehicle findByIdAndDeletedFalse(Long id);
}
