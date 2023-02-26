package com.toyota.cvqsfinal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VehicleDefect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String defectName;

    @OneToOne
    private Image image;

    @OneToMany
    @JoinColumn(name = "vehicleDefect_id")
    private List<DefectLocation> defectLocations;

    private boolean deleted;


}
