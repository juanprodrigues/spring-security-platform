package com.security.core.exception;

public class InvalidTokenException extends JwtException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String message) {
        super(message);
    }
}