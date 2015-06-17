package com.joymeter.security;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

public class JoymeterSecurityInterceptor implements Ordered {
	
	private static int ORDER = 1;
	
	private final static Logger logger = Logger.getLogger(JoymeterSecurityInterceptor.class);

	public int getOrder() {
		return ORDER;
	}
	
	public Object checkAccess(ProceedingJoinPoint pjp) throws Throwable {
		logger.warn("Checking access through aspect");
		
		JoymeterContext joymeterContext = JoymeterContextHolder.get();
		if (StringUtils.isEmpty(joymeterContext.getSessionToken())){
			throw new JoymeterUnauthorizedException();
		}
		
		Object result = pjp.proceed();
		return result;
	}

}
