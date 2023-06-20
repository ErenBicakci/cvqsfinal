package com.toyota.cvqsfinal.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DefectDto {

    private Long id;
    private ImageDto imageDto;
    private String name;


}
