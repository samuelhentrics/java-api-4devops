package com.api.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.api.exception.MrException;

import lombok.extern.log4j.Log4j2;

@Provider
@Log4j2
public class MrExceptionMapper implements ExceptionMapper<MrException> {

	@Override
	public Response toResponse(MrException exception) {
		log.warn(exception.getMessage(),exception);
		return exception.getResponse();
	}

}
