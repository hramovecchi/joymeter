package com.joymeter.api.client.dto;


public class GCMToAndroidClientDTO {
	
	private String to;
	private GCMAdviceContainer data;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public GCMAdviceContainer getData() {
		return data;
	}
	public void setData(GCMAdviceContainer data) {
		this.data = data;
	}
}
