package com.joymeter.api.client.dto;

import com.joymeter.entity.Activity;

public class GCMActivityContainer {

	private Activity activity;
	
	public GCMActivityContainer(Activity activity){
		this.setActivity(activity);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
