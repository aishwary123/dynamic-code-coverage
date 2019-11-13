package com.dynamic.codecoverage.exceptions;
public class RestClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;

	public RestClientException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
