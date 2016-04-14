package com.joymeter.service.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.joymeter.androidclient.JoymeterApp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hramovecchi on 09/04/2016.
 */
public class ConnectivityHelper {

    private static ConnectivityHelper helper;

    public static ConnectivityHelper getHelper(){
        if (helper == null){
            helper = new ConnectivityHelper();
        }
        return helper;
    }

    private static final String TAG = "ConnectivityHelper";

    public boolean hasInternetAccess() {
        if (isNetworkAvailable(JoymeterApp.getAppContext())) {
            //new CheckInternetTask().execute();
            return true;
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    class CheckInternetTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean hasInternetConnection) {
            super.onPostExecute(hasInternetConnection);
        }
    }
}
