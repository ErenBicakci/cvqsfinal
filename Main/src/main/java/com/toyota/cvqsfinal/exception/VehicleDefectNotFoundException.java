package com.toyota.cvqsfinal.exception;

public class VehicleDefectNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public VehicleDefectNotFoundException(String message) {
        super(message);
    }
}
