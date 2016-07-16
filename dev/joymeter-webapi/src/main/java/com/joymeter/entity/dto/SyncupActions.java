package com.joymeter.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SyncupActions {
	
	private List<SyncupActionDTO> syncupActions;
	
	public SyncupActions(){}
	
	public SyncupActions(List<SyncupActionDTO> syncupActions){
		this.setSyncupActions(syncupActions);
	}

	public List<SyncupActionDTO> getSyncupActions() {
		return syncupActions;
	}

	public void setSyncupActions(List<SyncupActionDTO> syncupActions) {
		this.syncupActions = syncupActions;
	}
}
