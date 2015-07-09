package com.joymeter.entity.util;

import com.joymeter.entity.User;
import com.joymeter.entity.dto.UserDTO;

public class UserUtils {

	public static User mappedToUser(User user, UserDTO dto){
		user.setFullName(dto.getFullName() != null ? dto.getFullName() : user.getFullName());
		user.setEmail(dto.getEmail() != null ? dto.getEmail() : dto.getEmail());
		user.setFacebookAccessToken(dto.getFacebookAccessToken() != null ? dto.getFacebookAccessToken() : dto.getFacebookAccessToken());
		user.setCreationDate(dto.getCreationDate() != null ? Long.parseLong(dto.getCreationDate()) : user.getCreationDate());
		
		return user;
	}
}
