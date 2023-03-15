package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.Defect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefectRepository extends JpaRepository<Defect,Long> {

    Defect getDefectByIdAndDeletedFalse(Long id);

    Page<Defect> getAllByDefectNameLikeAndDeletedFalse(String defectName, Pageable pageable);
}
