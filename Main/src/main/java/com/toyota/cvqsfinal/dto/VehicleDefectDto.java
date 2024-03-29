package com.toyota.cvqsfinal.dto;

import com.toyota.cvqsfinal.entity.DefectLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDefectDto {

    private Long id;
    private DefectDto defect;

    private List<DefectLocation> defectLocations;
}
