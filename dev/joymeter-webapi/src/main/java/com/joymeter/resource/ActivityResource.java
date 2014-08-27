package com.joymeter.resource;

import javax.ws.rs.core.Response;

import com.joymeter.entity.dto.ActivityDTO;

public interface ActivityResource {
	Response getActivities(String userId);
	Response addActivity(ActivityDTO activity);
	Response getActivity(String id);
	Response deleteActivity(String id);
	Response updateActivity(String id, ActivityDTO activity);
}
