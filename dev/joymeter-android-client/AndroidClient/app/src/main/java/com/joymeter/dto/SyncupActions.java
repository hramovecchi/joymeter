package com.joymeter.dto;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 * Created by hramovecchi on 25/08/2015.
 */
public class SyncupActions {

    @Expose
    private List<SyncupActionDTO> syncupActions;

    public SyncupActions(){}

    public SyncupActions(List<SyncupActionDTO> syncupActions){
        this.syncupActions = syncupActions;
    }

    /**
     *
     * @return
     * The syncupActions
     */
    public List<SyncupActionDTO> getSyncupActions() {
        return syncupActions;
    }

    /**
     *
     * @param syncupActions
     * The syncupActions
     */
    public void setSyncupActions(List<SyncupActionDTO> syncupActions) {
        this.syncupActions = syncupActions;
    }
}
