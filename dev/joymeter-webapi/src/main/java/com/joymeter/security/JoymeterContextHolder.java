package com.joymeter.security;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public abstract class JoymeterContextHolder {
	
	public static JoymeterContext get(){
		return (JoymeterContext) RequestContextHolder.getRequestAttributes().getAttribute(
				JoymeterContext.class.getName(), RequestAttributes.SCOPE_REQUEST);
	}
	
	public static void set(JoymeterContext context){
		RequestContextHolder.getRequestAttributes().setAttribute(
				JoymeterContext.class.getName(), context, RequestAttributes.SCOPE_REQUEST);
	}

}
