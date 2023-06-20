package com.toyota.cvqsfinal.exception;

import java.io.Serial;

public class VehicleDefectNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public VehicleDefectNotFoundException(String message) {
        super(message);
    }
}
