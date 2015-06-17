package com.joymeter.security;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.joymeter.service.SessionService;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Component
public class JoymeterContextFilter implements ContainerRequestFilter{
	
	private static String AUTHORIZATION_DELIMITER = "Bearer";
	
	@Autowired
	private SessionService sessionService;

	public ContainerRequest filter(ContainerRequest request) {
		
		JoymeterContextHolder.set(getContext(request));
		
		return request;
	}

	private JoymeterContext getContext(ContainerRequest request) {
		
		JoymeterContext joymeterContext = new JoymeterContext();
		
		String sessionToken = null;
		
		String [] parts = StringUtils.split(request.getHeaderValue(HttpHeaders.AUTHORIZATION), " ");
		
		if (parts != null && parts.length == 2 && AUTHORIZATION_DELIMITER.equals(parts[0])){
			sessionToken = parts[1];
		}
		
		if (!StringUtils.isEmpty(sessionToken)){
			joymeterContext.setSessionToken(sessionToken);
		}
		
		return joymeterContext;
	}

}
