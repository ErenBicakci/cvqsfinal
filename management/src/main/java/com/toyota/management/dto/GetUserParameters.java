package com.toyota.management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserParameters {
    private int page;
    private int pageSize;
    private String filterKeyword;
    private String sortType;
}