package com.toyota.cvqsfinal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toyota.cvqsfinal.dto.DefectDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Defect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String defectName;

    @OneToOne
    private Image image;

    private boolean deleted;


}
