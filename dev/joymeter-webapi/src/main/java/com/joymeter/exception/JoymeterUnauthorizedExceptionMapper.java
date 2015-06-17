package com.joymeter.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.joymeter.api.util.ResponseFactory;
import com.joymeter.security.JoymeterUnauthorizedException;
import com.sun.jersey.spi.resource.Singleton;

/**
 * {link ExceptionMapper} for {@link JoymeterUnauthorizedException}}
 * 
 * @author hramovecchi@gmail.com
 *
 */
@Provider
@Singleton
@Component
public class JoymeterUnauthorizedExceptionMapper implements
		ExceptionMapper<JoymeterUnauthorizedException> {
	
	private Logger log = Logger.getLogger(this.getClass());

	public Response toResponse(JoymeterUnauthorizedException exception) {
		log.error("Authentication Error: SessionId not Present");
		return ResponseFactory.unauthorized();
	}
}
