package com.api.exception.business;

import javax.ws.rs.core.Response.Status;

import com.api.exception.MrException;


public class InvalidElementException extends MrException{

	private static final long serialVersionUID = 6513048374826915234L;

	public InvalidElementException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidElementException(Throwable cause) {
		super(cause);
	}

	public InvalidElementException(String message) {
		super(message);
	}

	@Override
	public Status getHttpCode() {
		return Status.BAD_REQUEST;
	}
	
}
