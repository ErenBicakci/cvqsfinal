package com.toyota.cvqsfinal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private LocalDate date;
    private String message;
    private String type;
}
