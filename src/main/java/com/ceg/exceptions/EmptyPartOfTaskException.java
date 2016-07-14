package com.ceg.exceptions;

/**
 *
 * @author Martyna
 */
public class EmptyPartOfTaskException extends Exception {
    
    public EmptyPartOfTaskException() {
    }

    public EmptyPartOfTaskException(String message) {
        super(message);
    }

    public EmptyPartOfTaskException(Throwable cause) {
        super(cause);
    }

    public EmptyPartOfTaskException(String message, Throwable cause) {
        super(message, cause);  
    }
}
