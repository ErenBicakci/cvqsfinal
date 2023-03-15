package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {


    Image findByIdAndDeletedFalse(Long id);
}
