package com.toyota.cvqsfinal.exception;

import java.io.Serial;

public class VehicleNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
