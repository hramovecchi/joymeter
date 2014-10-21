package com.joymeter.resource;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.SignUpRequestDTO;
import com.joymeter.entity.util.FacebookUtils;
import com.joymeter.entity.util.SessionUtils;
import com.joymeter.service.SessionService;
import com.joymeter.service.UserService;
import com.mysql.jdbc.StringUtils;

@Component
@Path("/sessions")
@Scope("request")
public class SessionResourceWs implements SessionResource {
	@Autowired
	UserService userService;
	
	@Autowired
	SessionService sessionService;
	
	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response autenticate(@QueryParam("session_token") String sessionToken) {
		log.info("");
		
		return Response.ok("").build();
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUp(SignUpRequestDTO signUpRequestDTO){
		if (!StringUtils.isNullOrEmpty(signUpRequestDTO.getFacebookAccessToken())){
			log.info("Entering on singUp mode");
			//get the user from facebook with {facebookAccessToken} and add it to the database if not exists yet
			
			FacebookTemplate facebook = new FacebookTemplate(signUpRequestDTO.getFacebookAccessToken());
			FacebookProfile fbProfile;
			
			try {
				fbProfile = facebook.userOperations().getUserProfile();
			} catch (Exception e){
				log.error(String.format("Invalid Facebook Access Token: %s.",signUpRequestDTO.getFacebookAccessToken()));
				//TODO: need an error dto
				return Response.status(Status.BAD_REQUEST).entity("{'errorCode':'100','errorMesage':'Invalid Facebook Access Token.'}").build();
			}
			
			User user = userService.getByEmail(fbProfile.getEmail());
			
			if (user == null){	//it is a new user
				log.info(String.format("The user with email: %s doesn't exist.",fbProfile.getEmail()));
				user = new User();
				user.setCreationDate(new Date().getTime());
				user.setEmail(fbProfile.getEmail());
				user.setFullName(FacebookUtils.getFullName(
						fbProfile.getFirstName(), fbProfile.getMiddleName(),
						fbProfile.getLastName()));
			} else {
				//TODO: falta el codigo del delete X GCM
				sessionService.deleteByUserId(user.getId(),signUpRequestDTO.getGcmToken());
			}
			//update facebook access token
			user.setFacebookAccessToken(signUpRequestDTO.getFacebookAccessToken());
			
			Session sessionToStore = new Session();
			sessionToStore.setUser(user);			
			sessionToStore.setSessionToken(SessionUtils.generateSessionId());
			sessionToStore.setGcmToken(signUpRequestDTO.getGcmToken());
			
			sessionService.save(sessionToStore);
			Session savedSession = sessionService.getBySessionToken(sessionToStore.getSessionToken());
			
			return Response.ok(savedSession).build();
		}
		
		return Response.noContent().build();
	}
}
