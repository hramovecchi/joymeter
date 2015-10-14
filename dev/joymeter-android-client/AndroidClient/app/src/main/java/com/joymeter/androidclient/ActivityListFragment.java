package com.joymeter.androidclient;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.joymeter.androidclient.cursor.ActivityArrayAdapter;
import com.joymeter.dto.Activities;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.rest.ActivityService;
import com.joymeter.rest.factory.ActivityServiceFactory;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class ActivityListFragment extends ListFragment {

    private List<ActivityDTO> userActivities;
    private SharedPreferences preferences;
    private ActivityArrayAdapter activityArrayAdapter;

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
                    activityArrayAdapter = new ActivityArrayAdapter(getActivity(), activities.getActivities());
                    setListAdapter(activityArrayAdapter);

                    registerForContextMenu(getListView());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.history_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final ActivityDTO activity = userActivities.get(info.position);

        switch (item.getItemId()) {

            case R.id.edit:
                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.remove:
                ActivityService activityService = ActivityServiceFactory.getInstance();
                activityService.deleteActivity(activity.getId(), new ResponseCallback() {
                    @Override
                    public void success(Response response) {
                        Toast.makeText(getActivity(), "Remove sucessfull", Toast.LENGTH_SHORT).show();
                        activityArrayAdapter.remove(activity);
                        activityArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Remove failed", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}