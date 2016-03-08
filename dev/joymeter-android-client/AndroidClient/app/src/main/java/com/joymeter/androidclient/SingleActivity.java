package com.joymeter.androidclient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.joymeter.dto.ActivityAction;
import com.joymeter.dto.ActivityAction.SaveAction;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.events.bus.EventsBus;


public class SingleActivity extends FragmentActivity {

    private SaveAction saveAction = SaveAction.save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        if (getIntent().getBooleanExtra(JoymeterPreferences.JOYMETER_UPDATE_ACTION,Boolean.FALSE)){
            saveAction = SaveAction.update;
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
                    EventsBus.getInstance().post(new ActivityAction(activity, saveAction));
                    break;
                case update:
                    int position = getIntent().getIntExtra(JoymeterPreferences.JOYMETER_ACTIVITY_POSITION, -1);
                    EventsBus.getInstance().post(new ActivityAction(activity, saveAction, position));
                    break;
            }
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
