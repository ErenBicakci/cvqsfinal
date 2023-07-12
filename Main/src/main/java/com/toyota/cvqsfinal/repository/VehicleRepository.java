package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VehicleRepository extends JpaRepository<Vehicle,Long> {


    Page<Vehicle> findAllByCodeLikeAndModelNoLikeAndDeletedFalse(String code,String modelNo, Pageable pageable);

    Vehicle findVehicleByCode(String code);
    Vehicle findByIdAndDeletedFalse(Long id);


}
