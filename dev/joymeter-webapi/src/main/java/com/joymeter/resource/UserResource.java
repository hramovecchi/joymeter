package com.joymeter.resource;


import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Activity;
import com.joymeter.entity.HistoricalLevel;
import com.joymeter.entity.LevelOfJoy;
import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.UserDTO;
import com.joymeter.entity.util.ActivityUtils;
import com.joymeter.entity.util.UserUtils;
import com.joymeter.security.JoymeterContextHolder;
import com.joymeter.security.JoymeterUnauthorizedException;
import com.joymeter.security.RequiresAuthentication;

import com.joymeter.service.ActivityService;
import com.joymeter.service.NotificationService;
import com.joymeter.service.LevelOfJoyHistoricalService;
import com.joymeter.service.UserService;

@Component
@Path("/users")
@Scope("request")
public class UserResource{
	
	@Autowired
	UserService userService;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	NotificationService notificationService;

	@Autowired
	LevelOfJoyHistoricalService levelOfJoyHistoricalService;

	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getUser() {
		log.info("getUser entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		return Response.ok(user).build();
	}

	/*
	 */
	@PUT
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response updateUser(UserDTO userDTO) {
		log.info("updateUser entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();

		user = UserUtils.mappedToUser(user, userDTO);
		userService.update(user);
		return Response.ok(user).build();
	}
	
	@GET
	@Path("/me/suggest")
	@RequiresAuthentication
	public Response suggestActivity(){
		Session session = JoymeterContextHolder.get().getJoymeterSession();
		
		long userId = session.getUser().getId();
		Activity activityToSuggest = activityService.suggestActivity(userId);
		
		Activity aux = new Activity();
		aux.clone(activityToSuggest);
		
		long now = Calendar.getInstance().getTimeInMillis();
		
		aux.setStartDate(now);
		aux.setEndDate(now + ActivityUtils.durationToSuggest());
		
		notificationService.sendNotificationMessage(session.getGcmToken(), aux);
		
		return Response.ok("{}").build();
    }
		

	/*
	 */
	@GET
	@Path("/me/loj")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getLevelOfJoy(@QueryParam("days") int days) {
		
		log.info("getLevelOfJoy entered");
		
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		List<LevelOfJoy> historical = levelOfJoyHistoricalService.getLastEntriesByUser(user, days);
		
		return Response.ok(new HistoricalLevel(historical)).build();
	}
}
