package com.joymeter.androidclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.joymeter.androidclient.JoymeterPreferences;
import com.joymeter.androidclient.R;
import com.joymeter.dto.SignupRequestDTO;
import com.joymeter.dto.SignupResponseDTO;
import com.joymeter.rest.SessionService;
import com.joymeter.rest.factory.SessionServiceFactory;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 28/12/2015.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private SharedPreferences preferences;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String token = null;
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);

            subscribeTopics(token);

            preferences.edit().putBoolean(JoymeterPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);

            preferences.edit().putBoolean(JoymeterPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }

        boolean tokenUpdatedByGCM = intent.getBooleanExtra("tokenUpdatedByGCM", false);
        if (tokenUpdatedByGCM){
            SessionService sessionService = SessionServiceFactory.getInstance();
            String fbAccessToken = preferences.getString(JoymeterPreferences.FACEBOOK_TOKEN, "");
            String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            SignupRequestDTO signupRequestDTO= new SignupRequestDTO(fbAccessToken, token, deviceId);
            sessionService.createUser(signupRequestDTO, new Callback<SignupResponseDTO>() {
                @Override
                public void success(SignupResponseDTO signupResponseDTO, Response response) {
                    preferences.edit().putString(JoymeterPreferences.JOYMETER_TOKEN, signupResponseDTO.getSessionToken()).apply();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(RegistrationIntentService.this,
                            "Something went wrong calling updating session Joymeter API", Toast.LENGTH_LONG).show();
                }
            });
        }

        // Notify UI that registration has completed
        Intent registrationComplete = new Intent(JoymeterPreferences.REGISTRATION_COMPLETE);
        registrationComplete.putExtra(JoymeterPreferences.GCM_TOKEN, token);
        registrationComplete.putExtra(JoymeterPreferences.FACEBOOK_TOKEN, intent.getStringExtra(JoymeterPreferences.FACEBOOK_TOKEN));
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
