package com.joymeter.androidclient;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.joymeter.androidclient.cursor.ActivityArrayAdapter;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.events.bus.ActivitiesLoadedEvent;
import com.joymeter.events.bus.DeleteActivityEvent;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.events.bus.LoadActivitiesEvent;
import com.joymeter.utils.ActivityComparator;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

/**
 * Created by hramovecchi on 24/08/2015.
 */
public class ActivityListFragment extends ListFragment {

    private final int UPDATE_ACTIVITY = 2;

    private List<ActivityDTO> userActivities;
    private ActivityArrayAdapter activityArrayAdapter;
    private ActivityComparator comparator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comparator = new ActivityComparator();

        EventsBus.getInstance().register(this);
        EventsBus.getInstance().post(new LoadActivitiesEvent());
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
                Intent i = new Intent(getActivity(), SingleActivity.class);
                i.putExtra(JoymeterPreferences.JOYMETER_UPDATE_ACTION, Boolean.TRUE);
                i.putExtra(JoymeterPreferences.JOYMETER_ACTIVITY, activity);
                i.putExtra(JoymeterPreferences.JOYMETER_ACTIVITY_POSITION, info.position);

                startActivityForResult(i, UPDATE_ACTIVITY);
                return true;
            case R.id.remove:
                EventsBus.getInstance().post(new DeleteActivityEvent(activity));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        EventsBus.getInstance().unregister(this);
        super.onDestroy();
    }

    public void addActivity(ActivityDTO activity){
        activityArrayAdapter.add(activity);
        activityArrayAdapter.sort(comparator);
        activityArrayAdapter.notifyDataSetChanged();
    }

    public void insertActivity(ActivityDTO activity, int position){
        activityArrayAdapter.remove(activityArrayAdapter.getItem(position));
        activityArrayAdapter.insert(activity, position);
        activityArrayAdapter.sort(comparator);
        activityArrayAdapter.notifyDataSetChanged();
    }

    public void deleteActivity(ActivityDTO activity){
        Toast.makeText(getActivity(), "Remove sucessfull", Toast.LENGTH_SHORT).show();
        activityArrayAdapter.remove(activity);
        activityArrayAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onActivitiesLoaded(ActivitiesLoadedEvent event){
        userActivities = event.getActivities();
        Collections.sort(userActivities, comparator);
        activityArrayAdapter = new ActivityArrayAdapter(getActivity(), userActivities);
        setListAdapter(activityArrayAdapter);

        registerForContextMenu(getListView());
    }
}
