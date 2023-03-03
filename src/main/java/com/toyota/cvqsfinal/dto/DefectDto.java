package com.toyota.cvqsfinal.dto;

import com.toyota.cvqsfinal.entity.Defect;
import com.toyota.cvqsfinal.entity.DefectLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DefectDto {

    private Long id;
    private ImageDto imageDto;
    private String name;


}
