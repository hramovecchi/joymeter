package com.joymeter.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import com.joymeter.service.ActivityService;
import com.joymeter.service.UserService;
import com.mysql.jdbc.StringUtils;

@Component
@Path("/activities")
@Scope("request")
public class ActivityResourceWs implements ActivityResource {
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	UserService userService;
	
	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivities(@QueryParam("user_id") String userId) {
		if (!StringUtils.isNullOrEmpty(userId)){
			log.info("getActivities entered");
			log.info("userId: "+userId);
			
			List<Activity> activities = activityService.getByUserId(Long.parseLong(userId));
			
			return Response.ok(new Activities(activities)).build();
		}
		return Response.status(Status.BAD_REQUEST).build();
		
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addActivity(ActivityDTO activityDTO) {
		log.info("addActivity entered");

		Activity activity = activityDTO.mappedToActivity();
		User owner = userService.getById(Long.parseLong(activityDTO.getUserId()));
		activity.setUser(owner);
		
		activityService.save(activity);
		
		return Response.ok("").build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivity(@PathParam("id") String id) {
		log.info("getActivity entered");
		log.info("Hit getActivity");
		Activity activity = activityService.getById(Integer.parseInt(id));
		if (activity!= null){
//			log.info("Sending: \n\n" + activity.toString() + "\n\n");
			return Response.ok(activity).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteActivity(@PathParam("id") String id) {
		if( id != null) {
			log.info("Delete Activity Id: " + id);
			Activity activity = activityService.getById(Integer.parseInt(id));
			if (activity == null) {
				log.info("Null was returned for ID: " + id);
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				activityService.delete(activity);
				return Response.ok("").build();
			}
		}
		return Response.status(Status.BAD_REQUEST).build();
	}

	/*
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateActivity(@PathParam("id") String id, ActivityDTO activityDTO) {
		if (activityDTO != null){
			
			Activity updatedActivity = activityDTO.mappedToActivity();
			updatedActivity.setUser(userService.getById(Long.parseLong(activityDTO.getUserId())));
			updatedActivity.setId(Long.parseLong(id));
			
			activityService.update(updatedActivity);
			
			return Response.status(Status.OK).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
