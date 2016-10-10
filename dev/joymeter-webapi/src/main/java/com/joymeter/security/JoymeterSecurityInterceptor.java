package com.joymeter.security;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import com.joymeter.entity.Session;
import com.joymeter.repository.SessionRepository;

public class JoymeterSecurityInterceptor implements Ordered {
	
	private static int ORDER = 1;
	
	private final static Logger logger = Logger.getLogger(JoymeterSecurityInterceptor.class);
	
	@Value("${admin.username}")
	private String adminUserName;

	@Value("${admin.password}")
	private String adminPassword;

	@Value("${gcm.notification.uri}")
	private String baseUri;

	@Autowired
	private SessionRepository sessionService;

	public int getOrder() {
		return ORDER;
	}
	
	public Object checkAccess(ProceedingJoinPoint pjp) throws Throwable {
		logger.warn("Checking access through aspect");
		
		JoymeterContext joymeterContext = JoymeterContextHolder.get();
		if (StringUtils.isEmpty(joymeterContext.getSessionToken())){
			throw new JoymeterUnauthorizedException();
		}
		
		Session joymeterSession = sessionService.getBySessionToken(
				joymeterContext.getSessionToken());
		
		if (joymeterSession == null){
			throw new JoymeterInvalidSessionException();
		}
		joymeterContext.setJoymeterSession(joymeterSession);
		JoymeterContextHolder.set(joymeterContext);
		
		Object result = pjp.proceed();
		return result;
	}

	public Object checkAdminAllowed(ProceedingJoinPoint pjp) throws Throwable {
		JoymeterContext joymeterContext = JoymeterContextHolder.get();
		if (StringUtils.isEmpty(joymeterContext.getAdminUserName()) &&
				StringUtils.isEmpty(joymeterContext.getAdminPassword())){
			throw new JoymeterUnauthorizedException();
		}

		if (!joymeterContext.getAdminUserName().equals(adminUserName) ||
				!joymeterContext.getAdminPassword().equals(adminPassword)){
			throw new JoymeterUnauthorizedException();
		}

		Object result = pjp.proceed();
		return result;
	}

}
