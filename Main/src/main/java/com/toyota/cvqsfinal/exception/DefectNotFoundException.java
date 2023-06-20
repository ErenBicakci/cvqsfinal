package com.toyota.cvqsfinal.exception;

import java.io.Serial;

public class DefectNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public DefectNotFoundException(String message) {
        super(message);
    }
}
