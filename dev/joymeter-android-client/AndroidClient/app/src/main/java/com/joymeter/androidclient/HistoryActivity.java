package com.joymeter.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 15/09/2015.
 */
public class HistoryActivity extends FragmentActivity{

    private final int ADD_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent i = new Intent(getApplicationContext(), SingleActivity.class);
            startActivityForResult(i, ADD_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ACTIVITY){
            if (resultCode == RESULT_OK){
                ActivityDTO activity = (ActivityDTO)data.getSerializableExtra("ACTIVITY_ADDED");
                ActivityListFragment fragment = (ActivityListFragment)getFragmentManager().findFragmentById(R.id.activity_history_fragment);
                fragment.addActivity(activity);
            }else if (resultCode == RESULT_CANCELED){

            }
        }
    }
}
