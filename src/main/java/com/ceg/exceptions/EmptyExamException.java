/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.exceptions;

/**
 *
 * @author Martyna
 */
public class EmptyExamException extends Exception {
    
    public EmptyExamException() {
    }

    public EmptyExamException(String message) {
        super(message);
    }

    public EmptyExamException(Throwable cause) {
        super(cause);
    }

    public EmptyExamException(String message, Throwable cause) {
        super(message, cause);  
    }
}
