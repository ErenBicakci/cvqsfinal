package com.toyota.cvqsfinal.entity;

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
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelNo;

    private String code;
    @OneToMany
    @JoinColumn(name = "vehicle_id")
    private List<VehicleDefect> vehicleDefect;

    private boolean deleted;
}
