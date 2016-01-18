package com.joymeter.androidclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.joymeter.androidclient.service.RegistrationIntentService;
import com.joymeter.dto.SignupRequestDTO;
import com.joymeter.dto.SignupResponseDTO;
import com.joymeter.rest.SessionService;
import com.joymeter.rest.factory.SessionServiceFactory;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends FragmentActivity {

    CallbackManager callbackManager;
    private LoginButton loginBtn;
    private SharedPreferences preferences;
    private static Context context;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LoginActivity";

    public static Context getAppContext(){
        return context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String sessionToken = preferences.getString(JoymeterPreferences.JOYMETER_TOKEN, null);
        if (sessionToken != null){
            Intent intent =  new Intent(context, HistoryActivity.class);
            startActivity(intent);
            finish();

        } else {
            FacebookSdk.sdkInitialize(context);
            callbackManager = CallbackManager.Factory.create();

            setContentView(R.layout.login_activity);

            loginBtn = (LoginButton) findViewById(R.id.login_button);
            loginBtn.setReadPermissions(Arrays.asList("email"));
            loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    LocalBroadcastManager.getInstance(LoginActivity.this).registerReceiver(mRegistrationBroadcastReceiver,
                            new IntentFilter(JoymeterPreferences.REGISTRATION_COMPLETE));

                    if (checkPlayServices()){
                        Intent intent = new Intent(LoginActivity.this, RegistrationIntentService.class);
                        intent.putExtra(JoymeterPreferences.FACEBOOK_TOKEN, loginResult.getAccessToken().getToken());
                        startService(intent);
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(context, "Something went wrong calling Facebook API", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (mRegistrationBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }
        super.onDestroy();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
            boolean sentToken = sharedPreferences.getBoolean(JoymeterPreferences.SENT_TOKEN_TO_SERVER, false);

            if (sentToken) {
                final String gcmToken = intent.getStringExtra(JoymeterPreferences.GCM_TOKEN);
                final String fbAccessToken = intent.getStringExtra(JoymeterPreferences.FACEBOOK_TOKEN);

                SessionService sessionService = SessionServiceFactory.getInstance();
                SignupRequestDTO signupRequest = new SignupRequestDTO(fbAccessToken, gcmToken);

                sessionService.createUser(signupRequest, new Callback<SignupResponseDTO>() {
                    @Override
                    public void success(SignupResponseDTO signupResponseDTO, Response response) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(JoymeterPreferences.JOYMETER_TOKEN, signupResponseDTO.getSessionToken());
                        editor.putLong(JoymeterPreferences.USER_ID, signupResponseDTO.getUser().getId());
                        editor.commit();

                        Intent intent = new Intent(getAppContext(), HistoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(context, "Something went wrong calling Joymeter API", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(context, "Something went wrong calling GCM API", Toast.LENGTH_LONG).show();
            }
        }
    };
}