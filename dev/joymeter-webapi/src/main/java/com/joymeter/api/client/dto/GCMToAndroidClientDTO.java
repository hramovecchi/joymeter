package com.joymeter.api.client.dto;

import com.joymeter.entity.Activity;

public class GCMToAndroidClientDTO {
	
	private String to;
	private Activity data;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Activity getData() {
		return data;
	}
	public void setData(Activity data) {
		this.data = data;
	}

}
