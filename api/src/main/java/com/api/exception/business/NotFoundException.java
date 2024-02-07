package com.api.exception.business;

import javax.ws.rs.core.Response.Status;

import com.api.exception.MrException;

public class NotFoundException extends MrException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(String message) {
		super(message);
	}

	@Override
	public Status getHttpCode() {
		
		return Status.NOT_FOUND;
	}
}
