package com.joymeter.service;

import android.widget.Toast;

import com.joymeter.androidclient.JoymeterPreferences;
import com.joymeter.dto.LevelOfJoyHistory;
import com.joymeter.events.bus.ActivitiesLoadedEvent;
import com.joymeter.events.bus.LevelOfJoyLoadedEvent;
import com.joymeter.events.bus.LoadActivitiesEvent;
import com.joymeter.events.bus.LoadLevelOfJoyEvent;
import com.joymeter.events.bus.SuggestActivityEvent;
import com.joymeter.events.bus.SuggestActivityLoaded;
import com.joymeter.rest.UserService;
import com.joymeter.rest.factory.UserServiceFactory;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 16/06/2016.
 */
public class JUserService {

    private UserService api;
    private Bus bus;

    public JUserService(UserService api, Bus eventBus) {
        this.api = api;
        this.bus = eventBus;
    }

    @Subscribe
    public void onLoadLevelOfJoy(LoadLevelOfJoyEvent event){
        api.getLevelOfJoy(String.valueOf(JoymeterPreferences.LOJ_WINDOW_SIZE), new Callback<LevelOfJoyHistory>() {
            @Override
            public void success(LevelOfJoyHistory levelOfJoyHistory, Response response) {
                //trigger levelOfJoyLoadedEvent
                bus.post(new LevelOfJoyLoadedEvent(levelOfJoyHistory.getHistory()));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Subscribe
    public void onSuggestActivity(SuggestActivityEvent event){
        api.suggestActivity(new ResponseCallback() {
            @Override
            public void success(Response response) {
                bus.post(new SuggestActivityLoaded());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
