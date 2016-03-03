package com.joymeter.androidclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.rest.ActivityService;
import com.joymeter.rest.factory.ActivityServiceFactory;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SingleActivity extends FragmentActivity {

    private String saveAction = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        if (getIntent().getBooleanExtra("updateActivity",Boolean.FALSE)){
            saveAction = "update";
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            SingleActivityFragment fragment = (SingleActivityFragment)getFragmentManager().findFragmentById(R.id.single_joymeter_activity);
            final ActivityDTO activity = fragment.getActivityDTO();

            ActivityService activityService = ActivityServiceFactory.getInstance();

            if (saveAction.equals("add")) {
                activityService.addActivity(activity, new Callback<ActivityDTO>() {
                    @Override
                    public void success(ActivityDTO activityDTO, Response response) {
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ACTIVITY_ADDED", activityDTO);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }
                });
            } else if (saveAction.equals("update")){
                activityService.updateActivity(activity.getId(), activity, new Callback<ActivityDTO>() {
                    @Override
                    public void success(ActivityDTO activityDTO, Response response) {
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ACTIVITY_UPDATED", activityDTO);
                        returnIntent.putExtra("position", getIntent().getIntExtra("position", -1));
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }
                });
            }

            if (!activity.isClassified()){
                FacebookSdk.sdkInitialize(getApplicationContext());
                CallbackManager callbackManager = CallbackManager.Factory.create();
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Hello Facebook")
                            .setContentDescription(
                                    "The 'Hello Facebook' sample  showcases simple Facebook integration")
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();

                    shareDialog.show(linkContent);
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
