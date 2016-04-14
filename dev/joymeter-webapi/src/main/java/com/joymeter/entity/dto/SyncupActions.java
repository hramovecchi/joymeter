package com.joymeter.entity.dto;

import java.util.List;

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
