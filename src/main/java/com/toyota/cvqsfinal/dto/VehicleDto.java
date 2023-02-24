package com.toyota.cvqsfinal.dto;

import com.toyota.cvqsfinal.entity.VehicleDefect;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private Long id;

    private String modelNo;
    private String vehicleCode;

    private List<VehicleDefect> vehicleDefect;
}
