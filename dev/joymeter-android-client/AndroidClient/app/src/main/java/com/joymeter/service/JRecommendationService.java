package com.joymeter.service;

import com.joymeter.dao.UserActivityDao;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.AdviceDTO;
import com.joymeter.events.bus.AcceptRecommendationEvent;
import com.joymeter.events.bus.ActivityAddedEvent;
import com.joymeter.events.bus.SuggestActivityEvent;
import com.joymeter.events.bus.SuggestActivityLoaded;
import com.joymeter.rest.RecommendationService;
import com.joymeter.service.helper.ConnectivityHelper;
import com.joymeter.service.mapper.ActivityMapper;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.UUID;

import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 12/07/2016.
 */
public class JRecommendationService {

    private RecommendationService api;
    private Bus bus;
    private UserActivityDao activityDao;
    private JActivityService activityService;

    public JRecommendationService(RecommendationService api, Bus eventBus, UserActivityDao activityDao, JActivityService activityService){
        this.api = api;
        this.bus = eventBus;
        this.activityDao = activityDao;
        this.activityService = activityService;
    }

    @Subscribe
    public void onSuggestActivity(SuggestActivityEvent event){
        api.suggestActivity(event.getDatePicked(), new ResponseCallback() {
            @Override
            public void success(Response response) {
                bus.post(new SuggestActivityLoaded());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Subscribe
    public void onAcceptRecommendation(AcceptRecommendationEvent event){
        event.getView().finish();
        final AdviceDTO adviceDTO = event.getAdvice();
        final ActivityDTO activityToAdd = adviceDTO.getCreatedActivity();
        activityToAdd.setId(UUID.randomUUID().getLeastSignificantBits());

        //addOnDB
        activityDao.insert(ActivityMapper.mapToDB(activityToAdd));

        //acceptOnServer or saveForLater
        if (ConnectivityHelper.getHelper().hasInternetAccess()){
            api.acceptAdvice(adviceDTO, new ResponseCallback() {
                @Override
                public void success(Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    activityService.storeInBackupAction(activityToAdd, JActivityService.SyncupActionMethod.save);
                }
            });
            ;
        } else {
            //TODO check how syncupAction is saved
            activityService.storeInBackupAction(activityToAdd, JActivityService.SyncupActionMethod.save);
        }

        //trigger activityAddedEvent
        bus.post(new ActivityAddedEvent(activityToAdd));
    }
}
