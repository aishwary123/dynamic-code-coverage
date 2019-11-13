package com.dynamic.codecoverage.exceptions;

public class ServiceInitializationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public ServiceInitializationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
