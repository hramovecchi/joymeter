package com.joymeter.resource;

import javax.ws.rs.core.Response;

public interface ActivityResource {
	Response getActivities(String id);
	Response addActivity(String type, String summary, String description, String levelOfJoy, String startDate, String endDate, String userId, String classified);
	Response getActivity(String id);
	Response deleteActivity(String id);
	Response updateActivity(String id, String type, String summary, String description, String levelOfJoy, String startDate, String endDate, String userId, String classified);
}
