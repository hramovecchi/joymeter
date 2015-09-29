package com.joymeter.rest.factory;

import android.preference.PreferenceManager;

import com.joymeter.androidclient.LoginActivity;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class JoymeterRestAdapter {

    private static String JOYMETER_BASEURI = "https://joymeter-joymeterwebapi.rhcloud.com";
    private static RestAdapter restAdapter;

    public static RestAdapter getInstance(){
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(JOYMETER_BASEURI)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            String sessionToken = PreferenceManager.getDefaultSharedPreferences(LoginActivity.getAppContext()).getString("sessionToken", null);
                            if (sessionToken != null){
                                request.addHeader("Authorization", "Bearer " + sessionToken);
                            }
                        }
                    })
                    .build();
        }
        return restAdapter;
    }
}
