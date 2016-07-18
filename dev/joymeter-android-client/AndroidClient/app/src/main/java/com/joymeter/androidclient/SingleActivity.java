package com.joymeter.androidclient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.AdviceDTO;
import com.joymeter.events.bus.AcceptRecommendationEvent;
import com.joymeter.events.bus.AddActivityEvent;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.events.bus.UpdateActivityEvent;


public class SingleActivity extends FragmentActivity {

    private enum SaveAction{
        save, update
    }

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

            if (activity.getDescription() == null || "".equals(activity.getDescription()) ||
                    activity.getSummary() == null || "".equals(activity.getSummary()) ||
                    activity.getStartDate() == null ||
                    activity.getEndDate() == null ||
                    activity.getLevelOfJoy() == null || activity.getLevelOfJoy().equals(0) ||
                    activity.getType() == null){
                Toast.makeText(this,"Todo el formulario debe estar completo", Toast.LENGTH_SHORT).show();
               return true;
            }

            switch (saveAction){
                case save:
                    AdviceDTO advice = (AdviceDTO)getIntent().getSerializableExtra(JoymeterPreferences.JOYMETER_ADVICE);
                    if (advice != null){
                        advice.setCreatedActivity(activity);
                        EventsBus.getInstance().post(new AcceptRecommendationEvent(SingleActivity.this,advice));
                    }else {
                        EventsBus.getInstance().post(new AddActivityEvent(SingleActivity.this, activity));
                    }
                    break;
                case update:
                    int position = getIntent().getIntExtra(JoymeterPreferences.JOYMETER_ACTIVITY_POSITION, -1);
                    EventsBus.getInstance().post(new UpdateActivityEvent(SingleActivity.this, activity, position));
                    break;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
