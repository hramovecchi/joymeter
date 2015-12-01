package com.joymeter.androidclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
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
    private SessionService sessionService;
    private SharedPreferences preferences;
    private AccessTokenTracker accessTokenTracker;
    private static Context context;

    public static Context getAppContext(){
        return context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String sessionToken = preferences.getString("sessionToken", null);
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

            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {

                    sessionService = SessionServiceFactory.getInstance();

                    final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    I

                    SignupRequestDTO signupRequest = new SignupRequestDTO(currentAccessToken.getToken(), androidId);

                    sessionService.createUser(signupRequest, new Callback<SignupResponseDTO>() {
                        @Override
                        public void success(SignupResponseDTO signupResponseDTO, Response response) {

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("sessionToken", signupResponseDTO.getSessionToken());
                            editor.putLong("userId", signupResponseDTO.getUser().getId());
                            editor.commit();

                            Intent intent = new Intent(context, HistoryActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            };
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accessTokenTracker != null) {
            accessTokenTracker.stopTracking();
        }
    }
}