package com.api.exception.mapper;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.api.exception.ErrorResponse;

import lombok.extern.log4j.Log4j2;

@Provider
@Log4j2
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {@Override
	public Response toResponse(Exception exception) {
	log.error(exception.getMessage(),exception);
	ErrorResponse responce = new ErrorResponse(exception.getMessage());
	return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responce)
			.type(MediaType.APPLICATION_JSON).build();
}

}
