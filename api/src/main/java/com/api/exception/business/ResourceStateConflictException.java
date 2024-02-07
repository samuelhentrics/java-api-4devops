package com.api.exception.business;

import javax.ws.rs.core.Response.Status;

import com.api.exception.MrException;

public class ResourceStateConflictException extends MrException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceStateConflictException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ResourceStateConflictException(Throwable cause) {
		super(cause);
	}

	public ResourceStateConflictException(String message) {
		super(message);
	}

	@Override
	public Status getHttpCode() {
		return Status.CONFLICT;
	}
	
}
