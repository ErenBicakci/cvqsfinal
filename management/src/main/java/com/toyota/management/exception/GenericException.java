package com.toyota.management.exception;

import java.io.Serial;

public class GenericException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public GenericException(String message) {
        super(message);
    }
}
