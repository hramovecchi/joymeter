package com.joymeter.resource;

import java.util.ArrayList;
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

import com.joymeter.entity.Activity;
import com.joymeter.entity.ActivityType;
import com.joymeter.entity.User;
import com.joymeter.entity.dto.ActivityDTO;
import com.joymeter.entity.dto.SyncupActions;
import com.joymeter.security.JoymeterContextHolder;
import com.joymeter.security.RequiresAuthentication;
import com.joymeter.service.ActivityService;

@Component
@Path("/activities")
@Scope("request")
public class ActivityResource {

	
	@Autowired
	ActivityService activityService;
	
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
		
		return Response.ok(activityService.getActivities(user)).build();
	}
	
	@GET
	@Path("/types")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response getTypes(){
		List<String> types = new ArrayList<String>();
		for (ActivityType type: ActivityType.values()){
			types.add(type.toString());
		}
		return Response.ok(types).build();
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response addActivity(ActivityDTO activityDTO) {
		log.info("addActivity entered");
		
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		return Response.ok(activityService.addActivity(owner, activityDTO)).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication()
	public Response getActivity(@PathParam("id") long activityId) {
		log.info("getActivity entered");
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		Activity activity = activityService.getActivity(activityId, owner);
		
		return Response.ok(activity).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response deleteActivity(@PathParam("id") long activityId) {
		log.info("Delete Activity Id: " + activityId);
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		activityService.deleteActivity(activityId, owner);
		
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
		
		return Response.ok(activityService.updateActivity(activityId, owner, activityDTO)).build();
	}
	/*
	 * This method is used from the client side to syncup with the server 
	 * when there was connectivity issues
	 * 
	 * @param the SyncupActions to syncup 
	 * 
	 * */
	@POST
	@Path("/syncup")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresAuthentication
	public Response syncup(SyncupActions syncupActions){
		User owner = JoymeterContextHolder.get().getJoymeterSession().getUser();
		
		activityService.syncup(syncupActions, owner);
		
		return Response.ok().build();
	}
}
