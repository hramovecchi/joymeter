package com.joymeter.androidclient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.joymeter.dto.ActivityAction;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.events.bus.EventsBus;


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
        int id = item.getItemId();

        if (id == R.id.action_save) {
            SingleActivityFragment fragment = (SingleActivityFragment)getFragmentManager().findFragmentById(R.id.single_joymeter_activity);
            final ActivityDTO activity = fragment.getActivityDTO();

            if (saveAction.equals("add")) {
                EventsBus.getInstance().post(new ActivityAction(activity, ActivityAction.SaveAction.save));

            } else if (saveAction.equals("update")){
                EventsBus.getInstance().post(new ActivityAction(activity, ActivityAction.SaveAction.update));
            }

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
