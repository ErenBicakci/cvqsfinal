package com.toyota.cvqsfinal.exception;

public class DefectNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DefectNotFoundException(String message) {
        super(message);
    }
}
