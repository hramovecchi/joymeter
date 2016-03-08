package com.joymeter.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joymeter.dto.ActivityAction;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.rest.ActivityService;
import com.joymeter.rest.UserService;
import com.joymeter.rest.factory.ActivityServiceFactory;
import com.joymeter.rest.factory.UserServiceFactory;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 15/09/2015.
 */
public class HistoryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        EventsBus.getInstance().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent i = new Intent(getApplicationContext(), SingleActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_chart) {
            UserService userService = UserServiceFactory.getInstance();
            userService.suggestActivity(new ResponseCallback() {
                @Override
                public void success(Response response) {
                    Toast.makeText(getApplicationContext(), "Calling Joymeter API to suggest an activity", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong calling suggest on Joymeter API", Toast.LENGTH_LONG).show();
                }
            });

            Intent i = new Intent(getApplicationContext(), ChartActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventsBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void activityCallback(final ActivityAction activityAction){
        ActivityService activityService = ActivityServiceFactory.getInstance();
        switch (activityAction.getSaveAction()){
            case save:
                activityService.addActivity(activityAction.getActivity(), new Callback<ActivityDTO>() {
                    @Override
                    public void success(ActivityDTO activityDTO, Response response) {
                        ActivityListFragment fragment = (ActivityListFragment) getFragmentManager().findFragmentById(R.id.activity_history_fragment);
                        fragment.addActivity(activityDTO);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                break;
            case update:
                ActivityDTO activityToUpdate = activityAction.getActivity();
                activityService.updateActivity(activityToUpdate.getId(), activityToUpdate, new Callback<ActivityDTO>() {
                    @Override
                    public void success(ActivityDTO activityDTO, Response response) {
                        ActivityListFragment fragment = (ActivityListFragment)getFragmentManager().findFragmentById(R.id.activity_history_fragment);
                        fragment.insertActivity(activityDTO, activityAction.getPosition());
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                break;
        }

        /*if (!activityAction.getActivity().isClassified()){
            FacebookSdk.sdkInitialize(getApplicationContext());
            CallbackManager callbackManager = CallbackManager.Factory.create();
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {}

                @Override
                public void onCancel() {}

                @Override
                public void onError(FacebookException error) {}
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
        }*/
    }
}
