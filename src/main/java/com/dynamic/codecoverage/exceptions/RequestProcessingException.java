package com.dynamic.codecoverage.exceptions;

public class RequestProcessingException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public RequestProcessingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
