package com.joymeter.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.SignUpRequestDTO;
import com.joymeter.entity.util.FacebookUtils;
import com.joymeter.entity.util.SessionUtils;
import com.joymeter.exception.ErrorCode;
import com.joymeter.repository.SessionRepository;
import com.joymeter.repository.UserRepository;
import com.joymeter.security.JoymeterBadRequestException;

@Service("sessionService")
public class SessionService {
	
	@Autowired
	UserRepository userService;
	
	@Autowired
	SessionRepository sessionRepository;

	public Session signUp(SignUpRequestDTO signUpRequestDTO){

		FacebookTemplate facebook = new FacebookTemplate(signUpRequestDTO.getFacebookAccessToken());
		FacebookProfile fbProfile;
		
		try {
			fbProfile = facebook.userOperations().getUserProfile();
		} catch (InvalidAuthorizationException e){
			if(e.getMessage().contains("expire")){
				throw new JoymeterBadRequestException(ErrorCode.EXPIRED_FACEBOOK_TOKEN);
			} else {
				throw new JoymeterBadRequestException(ErrorCode.INVALID_FACEBOOK_TOKEN);
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
			sessionRepository.deleteByDeviceId(signUpRequestDTO.getDeviceId());
		}
		//update facebook access token
		user.setFacebookAccessToken(signUpRequestDTO.getFacebookAccessToken());
		
		Session sessionToStore = new Session();
		sessionToStore.setUser(user);			
		sessionToStore.setSessionToken(SessionUtils.randomSessionToken());
		sessionToStore.setGcmToken(signUpRequestDTO.getGcmToken());
		sessionToStore.setDeviceId(signUpRequestDTO.getDeviceId());
		
		userService.save(user);
		sessionRepository.save(sessionToStore);
		
		return sessionToStore;
	}
}
