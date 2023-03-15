package com.toyota.cvqsfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetVehicleParameters {

    private String modelNo;
    private String vehicleCode;
    private int page;
    private int pageSize;
    private String sortType;
}

