package com.joymeter.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.joymeter.dto.ActivityAction;
import com.joymeter.dto.ActivityAction.SaveAction;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.rest.ActivityService;
import com.joymeter.rest.factory.ActivityServiceFactory;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SingleActivity extends FragmentActivity {

    private SaveAction saveAction = SaveAction.save;
    private boolean notificationCall = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        if (getIntent().getBooleanExtra(JoymeterPreferences.JOYMETER_UPDATE_ACTION,Boolean.FALSE)){
            saveAction = SaveAction.update;
        }

        notificationCall = getIntent().getBooleanExtra(JoymeterPreferences.JOYMETER_NOTIFICATION_CALL,Boolean.FALSE);
        if (notificationCall){
            EventsBus.getInstance().register(this);
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
        int id = item.getItemId();

        if (id == R.id.action_save) {
            SingleActivityFragment fragment = (SingleActivityFragment)getFragmentManager().findFragmentById(R.id.single_joymeter_activity);
            final ActivityDTO activity = fragment.getActivityDTO();

            switch (saveAction){
                case save:
                    EventsBus.getInstance().post(new ActivityAction(SingleActivity.this, activity, saveAction));
                    break;
                case update:
                    int position = getIntent().getIntExtra(JoymeterPreferences.JOYMETER_ACTIVITY_POSITION, -1);
                    EventsBus.getInstance().post(new ActivityAction(SingleActivity.this, activity, saveAction, position));
                    break;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (notificationCall) {
            EventsBus.getInstance().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe
    public void activityCallback(final ActivityAction activityAction){
        ActivityService activityService = ActivityServiceFactory.getInstance();
        activityService.addActivity(activityAction.getActivity(), new Callback<ActivityDTO>() {
            @Override
            public void success(ActivityDTO activityDTO, Response response) {
                Intent intent =  new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        //TODO share on facebook
    }
}
