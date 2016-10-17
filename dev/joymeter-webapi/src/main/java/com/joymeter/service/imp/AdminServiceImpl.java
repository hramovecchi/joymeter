package com.joymeter.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.repository.SessionRepository;
import com.joymeter.repository.UserRepository;
import com.joymeter.service.AdminService;
import com.joymeter.service.UserService;
import com.joymeter.service.imp.recommender.base.WekaBaseRecommender;

@Service("adminService")
public class AdminServiceImpl implements AdminService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	WekaBaseRecommender wekaBaseRecommender;

	public AdminServiceImpl() {}

	public AdminServiceImpl(UserRepository ur, UserService us, SessionRepository sr, WekaBaseRecommender wbr) {
		this.userRepository = ur;
		this.userService = us;
		this.sessionRepository = sr;
		this.wekaBaseRecommender = wbr;
	}

	public void suggestAllUsers() {
		List<User> userList = userRepository.getAll();

		for (User user : userList) {
			List<Session> userSessionsList = sessionRepository.getByUserId(user.getId());
			userService.suggestActivity(user, userSessionsList.get(0).getGcmToken());
		}
	}

	public void generateWekaModels(){
		List<User> userList = userRepository.getAll();

		for (User user : userList){
			wekaBaseRecommender.getUserInstances(user);
		}
	}
}
