package com.jwt;

public class JWTException extends Exception{

	private static final long serialVersionUID = 3543354160176829925L;

	public JWTException(String message, Throwable cause) {
		super(message, cause);
	}

	public JWTException(Throwable cause) {
		super(cause);
	}

	public JWTException(String message) {
		super(message);
	}
	
}
