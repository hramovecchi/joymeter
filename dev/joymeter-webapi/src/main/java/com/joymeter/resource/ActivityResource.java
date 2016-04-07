package com.joymeter.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.joymeter.entity.Activities;
import com.joymeter.entity.Activity;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.ActivityDTO;
import com.joymeter.entity.util.ActivityUtils;
import com.joymeter.security.JoymeterContextHolder;
import com.joymeter.security.JoymeterUnauthorizedException;
import com.joymeter.security.RequiresAuthentication;
import com.joymeter.service.ActivityService;
import com.joymeter.service.LevelOfJoyHistoricalService;
import com.joymeter.service.UserService;

@Component
@Path("/activities")
@Scope("request")
public class ActivityResource {
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	LevelOfJoyHistoricalService levelOfJoyHistoricalService;
	
	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getActivities() {
		User user = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		log.info("getActivities entered");
		log.info("userId: "+user.getId());
		
		List<Activity> activities = activityService.getByUserId(user.getId());
		
		return Response.ok(new Activities(activities)).build();
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response addOrUpdateActivity(ActivityDTO activityDTO) {
		log.info("addOrUpdateActivity entered");
		
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();

		Activity activity = new Activity();
		activity = ActivityUtils.mappedToActivity(activity, activityDTO);
		activity.setUser(owner);
		
		levelOfJoyHistoricalService.updateHistoryNewActivity(activity);
		
		if (activityDTO.getId() == null){
			activityService.save(activity);
		} else {
			activityService.update(activity);
		}
		
		return Response.ok(activity).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication()
	public Response getActivity(@PathParam("id") long activityId) {
		log.info("getActivity entered");
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		Activity activity = activityService.getById(activityId);
		
		if (activity == null){
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (owner.getId() != activity.getUser().getId()){
			throw new JoymeterUnauthorizedException();
		}
		
		return Response.ok(activity).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response deleteActivity(@PathParam("id") long activityId) {
		log.info("Delete Activity Id: " + activityId);
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		Activity activity = activityService.getById(activityId);
		
		if (activity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (owner.getId() != activity.getUser().getId()){
			throw new JoymeterUnauthorizedException();
		}
		activityService.delete(activity);
		levelOfJoyHistoricalService.updateHistoryDeleteActivity(activity);
		
		return Response.ok().build();
	}

	/*
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response updateActivity(@PathParam("id") long activityId, ActivityDTO activityDTO) {
		log.info("Update Activity Id: " + activityId);
		if (activityDTO == null){
			return Response.status(Status.BAD_REQUEST).build(); 
		}
		
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		Activity activity = activityService.getById(activityId);
		if (activity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (owner.getId() != activity.getUser().getId()){
			throw new JoymeterUnauthorizedException();
		}
		
		activity = ActivityUtils.mappedToActivity(activity, activityDTO);
		
		activityService.update(activity);
		levelOfJoyHistoricalService.updateHistoryUpdateActivity(activity);
		
		return Response.ok(activity).build();
	}
}
