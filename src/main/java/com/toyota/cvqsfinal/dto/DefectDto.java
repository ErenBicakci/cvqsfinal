package com.toyota.cvqsfinal.dto;

import com.toyota.cvqsfinal.entity.DefectLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DefectDto {

    private ImageDto imageDto;
    private String name;

}
