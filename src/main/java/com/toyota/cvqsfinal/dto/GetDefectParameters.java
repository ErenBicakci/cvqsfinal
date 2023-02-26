package com.toyota.cvqsfinal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetDefectParameters {
    private int page;
    private int pageSize;
    private String filterKeyword;
    private String sortType;
}
