package com.joymeter.events.bus;

import android.app.Activity;

import com.joymeter.dto.AdviceDTO;

/**
 * Created by hramovecchi on 12/07/2016.
 */
public class AcceptRecommendationEvent {

    private final Activity view;
    private final AdviceDTO advice;

    public AcceptRecommendationEvent(Activity view, AdviceDTO advice){
        this.view = view;
        this.advice = advice;
    }

    public Activity getView(){
        return view;
    }

    public AdviceDTO getAdvice(){
        return advice;
    }
}
