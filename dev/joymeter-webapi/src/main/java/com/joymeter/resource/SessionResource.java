package com.joymeter.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.joymeter.api.util.ResponseFactory;
import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.SignUpRequestDTO;
import com.joymeter.entity.util.FacebookUtils;
import com.joymeter.entity.util.SessionUtils;
import com.joymeter.exception.ErrorCode;
import com.joymeter.service.SessionService;
import com.joymeter.service.UserService;

@Component
@Path("/sessions")
@Scope("request")
public class SessionResource {
	@Autowired
	UserService userService;
	
	@Autowired
	SessionService sessionService;
	
	private Logger log = Logger.getLogger(this.getClass());

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signUp(SignUpRequestDTO signUpRequestDTO){
		if (!StringUtils.isEmpty(signUpRequestDTO.getFacebookAccessToken()) && 
				!StringUtils.isEmpty(signUpRequestDTO.getFacebookAccessToken())){
			
			FacebookTemplate facebook = new FacebookTemplate(signUpRequestDTO.getFacebookAccessToken());
			FacebookProfile fbProfile;
			
			try {
				fbProfile = facebook.userOperations().getUserProfile();
			} catch (InvalidAuthorizationException e){
				if(e.getMessage().contains("expire")){
					return ResponseFactory.badRequest(ErrorCode.EXPIRED_FACEBOOK_TOKEN);
				} else {
					return ResponseFactory.badRequest(ErrorCode.INVALID_FACEBOOK_TOKEN);
				}				
			}
			
			User user = userService.getByEmail(fbProfile.getEmail());
			
			if (user == null){	//it is a new user
				user = new User();
				user.setCreationDate(new Date().getTime());
				user.setEmail(fbProfile.getEmail());
				user.setFullName(FacebookUtils.getFullName(
						fbProfile.getFirstName(), fbProfile.getMiddleName(),
						fbProfile.getLastName()));
			} else {
				sessionService.deleteByDeviceId(signUpRequestDTO.getDeviceId());
			}
			//update facebook access token
			user.setFacebookAccessToken(signUpRequestDTO.getFacebookAccessToken());
			
			Session sessionToStore = new Session();
			sessionToStore.setUser(user);			
			sessionToStore.setSessionToken(SessionUtils.randomSessionToken());
			sessionToStore.setGcmToken(signUpRequestDTO.getGcmToken());
			sessionToStore.setDeviceId(signUpRequestDTO.getDeviceId());
			
			userService.save(user);
			sessionService.save(sessionToStore);
			
			return Response.ok(sessionToStore).build();
		}
		
		return Response.noContent().build();
	}
}
