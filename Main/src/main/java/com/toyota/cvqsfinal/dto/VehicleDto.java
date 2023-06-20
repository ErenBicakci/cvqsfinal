package com.toyota.cvqsfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private Long id;

    private String modelNo;
    private String vehicleCode;

    private List<VehicleDefectDto> vehicleDefectDtos;
}
