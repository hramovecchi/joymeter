package com.joymeter.resource;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.SignUpRequestDTO;
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
			FacebookProfile fbProfile = facebook.userOperations().getUserProfile();
			//TODO: confirm that email never comes with null value.
			User user = userService.getByFacebookAccessToken(fbProfile.getEmail());
			
			if (user == null){	//it is a new user
				log.info("EL USUARIO NO EXISTE EN LA BASE DE DATOS");
				//TODO obtain the user details from facebook
				user = new User();
				user.setCreationDate(new Date().getTime());
				user.setEmail(fbProfile.getEmail());
				user.setFacebookAccessToken(signUpRequestDTO.getFacebookAccessToken());
				user.setFullName(String.format(fbProfile.getFirstName()," ",fbProfile.getLastName()));
				userService.save(user);
			} else {
				user.setFacebookAccessToken(signUpRequestDTO.getFacebookAccessToken());
				userService.update(user);
				log.info("EL USUARIO EXISTE EN LA BASE DE DATOS");
			}
			
			Session sessionToStore = new Session();
			sessionToStore.setUser(user);
			sessionToStore.setSessionToken(signUpRequestDTO.getFacebookAccessToken()+"_SESSION_TOKEN");
			sessionToStore.setGcmToken(signUpRequestDTO.getFacebookAccessToken()+"_GCM_TOKEN");
			
			sessionService.save(sessionToStore);
			
			return Response.ok(sessionToStore).build();
		}
		
		return Response.noContent().build();
	}
}
