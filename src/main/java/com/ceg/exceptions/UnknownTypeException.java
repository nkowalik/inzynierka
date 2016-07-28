package com.ceg.exceptions;

/**
 *
 * @author Martyna
 */
public class UnknownTypeException extends Exception {
    public UnknownTypeException() {
    }

    public UnknownTypeException(String message) {
        super(message);
    }

    public UnknownTypeException(Throwable cause) {
        super(cause);
    }

    public UnknownTypeException(String message, Throwable cause) {
        super(message, cause);  
    }
}
