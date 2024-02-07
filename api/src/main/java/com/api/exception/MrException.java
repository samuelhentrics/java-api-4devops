package com.api.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public abstract class MrException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MrException(String message, Throwable cause) {
		super(message, cause);
	}

	public MrException(Throwable cause) {
		super(cause);
	}

	public MrException(String message) {
		super(message);
	}
	
	public abstract Status getHttpCode();
	
	public Response getResponse() {
		ErrorResponse responce = new ErrorResponse(super.getMessage());
		return Response.status(getHttpCode()).entity(responce)
				.type(MediaType.APPLICATION_JSON).build();
	}
}
