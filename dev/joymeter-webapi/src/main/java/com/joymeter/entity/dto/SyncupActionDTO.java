package com.joymeter.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SyncupActionDTO implements Serializable{

	private static final long serialVersionUID = -6710449603222567227L;
	
	public enum SyncupActionMethod{
		save, update, delete
	}
	
	private ActivityDTO activity;
	private SyncupActionMethod syncupActionMethod;
	
	public ActivityDTO getActivity() {
		return activity;
	}
	public void setActivity(ActivityDTO activity) {
		this.activity = activity;
	}
	public SyncupActionMethod getSyncupActionMethod() {
		return syncupActionMethod;
	}
	public void setSyncupActionMethod(SyncupActionMethod syncupActionMethod) {
		this.syncupActionMethod = syncupActionMethod;
	}

}
