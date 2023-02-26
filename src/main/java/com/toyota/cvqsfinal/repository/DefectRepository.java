package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.Defect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefectRepository extends JpaRepository<Defect,Long> {

    Defect getDefectByIdAndDeletedFalse(Long id);
}
