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

import com.joymeter.entity.Activity;
import com.joymeter.service.ActivityService;

@Component
@Path("/activities")
@Scope("request")
public class ActivityResourceWs implements ActivityResource {
	
	@Autowired
	ActivityService activityService;
	
	private Logger log = Logger.getLogger(this.getClass());

	/*
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivities(@QueryParam("user_id") String userId) {
		log.info("getActivities entered");
		log.info("userId: "+userId);
		
		StringBuffer jsonBuffer = new StringBuffer("{ \"activities\": [");
		List<Activity> activities = activityService.getAll(Integer.parseInt(userId));
		boolean first = true;
		for (Activity activity : activities) {
			if (first)
				first = false;
			else
				jsonBuffer.append(",");
			jsonBuffer.append(getJsonActivityObject(activity));
		}
		jsonBuffer.append("]}");
		log.info("Sending: \n\n" + jsonBuffer.toString() + "\n\n");
		
		return Response.ok(jsonBuffer.toString()).build();
	}

	/*
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addActivity(@QueryParam("type") String type, @QueryParam("summary") String summary, 
			@QueryParam("description") String description, @QueryParam("level_of_joy") String levelOfJoy,
			@QueryParam("start_date") String startDate, @QueryParam("end_date") String endDate, @QueryParam("user_id") String userId,
			@QueryParam("classified") String classified) {

		log.info("addActivity entered");

		log.info("type: " + type);
		log.info("summary: " + summary);
		log.info("description: " + description);
		log.info("level_of_joy : " + levelOfJoy);
		log.info("start_date : " + startDate);
		log.info("end_date: " + endDate);
		log.info("user_id : " + userId);
		log.info("classified : " + classified);

		Activity activity = new Activity();
		
		activity.setType(type);
		activity.setSummary(summary);
		activity.setDescription(description);
		activity.setLevelOfJoy(Integer.valueOf(levelOfJoy));
		activity.setStartDate(Long.valueOf(startDate));
		activity.setEndDate(Long.valueOf(endDate));
		activity.setUserId(Integer.valueOf(userId));
		activity.setClassified(Boolean.valueOf(classified));
		
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
			String result = (String) getJsonActivityObject(activity);
			log.info("Sending: \n\n" + result + "\n\n");
			return Response.ok(result.toString()).build();
		} else {
			return Response.status(Status.BAD_REQUEST).entity("Activity not found with the given id").build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteActivity(@PathParam("id") String id) {
		if(id!=null) {
			log.info("Delete Activity Id: " + id);
			Activity activity = activityService.getById(Integer.parseInt(id));
			if (activity == null) {
				log.info("Null was returned for ID: " + id);
				return Response.status(Status.BAD_REQUEST).entity("Activity not found with the given id").build();
			} else {
				activityService.delete(activity);
				return Response.ok("").build();
			}
		}
		return Response.status(Status.BAD_REQUEST).entity("id is null").build();
	}

	/*
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateActivity(@PathParam("id") String id, @QueryParam("type") String type, @QueryParam("summary") String summary, 
			@QueryParam("description") String description, @QueryParam("level_of_joy") String levelOfJoy,
			@QueryParam("start_date") String startDate, @QueryParam("end_date") String endDate, @QueryParam("user_id") String userId,
			@QueryParam("classified") String classified) {
		
		log.info("updateActivity entered");

		log.info("type: " + type);
		log.info("summary: " + summary);
		log.info("description: " + description);
		log.info("level_of_joy : " + levelOfJoy);
		log.info("start_date : " + startDate);
		log.info("end_date: " + endDate);
		log.info("user_id : " + userId);
		log.info("classified : " + classified);
		
		if (id!=null){
			Activity activity = activityService.getById(Integer.parseInt(id));
			if (activity != null) {
				log.info("It has an ID");
				log.info("We found the activity with that id");
				
				activity.setType(type);
				activity.setSummary(summary);
				activity.setDescription(description);
				activity.setLevelOfJoy(Integer.valueOf(levelOfJoy));
				activity.setStartDate(Long.valueOf(startDate));
				activity.setEndDate(Long.valueOf(endDate));
				activity.setUserId(Integer.valueOf(userId));
				activity.setClassified(Boolean.valueOf(classified));
				
				activityService.update(activity);
				return Response.ok("").build();
			} else {
				return Response.status(Status.BAD_REQUEST).entity("Activity not found with the given id").build();
			}
		} else {
			return Response.status(Status.BAD_REQUEST).entity("id not given").build();
		}
	}
	
	private Object getJsonActivityObject(Activity activity) {
		StringBuffer buffer = new StringBuffer("{\"id\": \"" + activity.getId());
		buffer.append("\",\"type\": \"").append(activity.getType());
		buffer.append("\",\"summary\": \"").append(activity.getSummary());
		buffer.append("\",\"description\": \"").append(activity.getDescription());
		buffer.append("\",\"level_of_joy\": \"").append(activity.getLevelOfJoy());
		buffer.append("\",\"start_date\": \"").append(activity.getStartDate());
		buffer.append("\",\"end_date\": \"").append(activity.getEndDate());
		buffer.append("\",\"user_id\": \"").append(activity.getUserId());
		buffer.append("\",\"classified\": \"").append(activity.isClassified());
		buffer.append("\"}");
		return buffer.toString();
	}

}
