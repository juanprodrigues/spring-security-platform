package com.security.core.exception;

public class ExpiredTokenException extends JwtException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpiredTokenException(String message) {
        super(message);
    }
}