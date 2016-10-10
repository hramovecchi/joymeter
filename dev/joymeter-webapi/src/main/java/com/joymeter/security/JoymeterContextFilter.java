package com.joymeter.security;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Component
public class JoymeterContextFilter implements ContainerRequestFilter{
	
	private static String AUTHORIZATION_DELIMITER = "Bearer";

	public ContainerRequest filter(ContainerRequest request) {
		
		JoymeterContextHolder.set(getContext(request));
		
		return request;
	}

	private JoymeterContext getContext(ContainerRequest request) {
		
		JoymeterContext joymeterContext = new JoymeterContext();
		
		String sessionToken = null;
		
		String [] parts = StringUtils.split(request.getHeaderValue(HttpHeaders.AUTHORIZATION), " ");
		
		if (parts != null && parts.length == 2 && AUTHORIZATION_DELIMITER.equals(parts[0])){
			String[] bearerParts = StringUtils.split(parts[1], ":");
			if (bearerParts != null){
				joymeterContext.setAdminUserName(bearerParts[0]);
				joymeterContext.setAdminPassword(bearerParts[1]);
			} else {
				sessionToken = parts[1];
			}

		}
		
		if (!StringUtils.isEmpty(sessionToken)){
			joymeterContext.setSessionToken(sessionToken);
		}
		
		return joymeterContext;
	}

}
