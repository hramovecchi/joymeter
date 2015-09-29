package com.joymeter.androidclient;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.joymeter.androidclient.cursor.ActivityArrayAdapter;
import com.joymeter.dto.Activities;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.rest.factory.ActivityServiceFactory;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class ActivityListFragment extends ListFragment {

    private List<ActivityDTO> userActivities;
    private SharedPreferences preferences;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Long userId = preferences.getLong("userId", 0);

        if (!userId.equals(0)) {
            ActivityServiceFactory.getInstance().getActivities(userId, new Callback<Activities>() {
                @Override
                public void success(Activities activities, Response response) {

                    userActivities = activities.getActivities();
                    setListAdapter(new ActivityArrayAdapter(getActivity(), activities.getActivities()));
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ActivityDTO activity = userActivities.get(position);

        Toast.makeText(getActivity(), activity.getSummary(), Toast.LENGTH_SHORT).show();
    }
}
