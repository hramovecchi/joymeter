package com.joymeter.api.client.dto;


public class GCMToAndroidClientDTO {
	
	private String to;
	private GCMActivityContainer data;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public GCMActivityContainer getData() {
		return data;
	}
	public void setData(GCMActivityContainer data) {
		this.data = data;
	}
}
