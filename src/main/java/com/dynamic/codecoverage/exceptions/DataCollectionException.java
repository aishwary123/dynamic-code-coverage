package com.dynamic.codecoverage.exceptions;

public class DataCollectionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public DataCollectionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
