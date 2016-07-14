package com.joymeter.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.AdviceDTO;
import com.joymeter.events.bus.ActivityAddedEvent;
import com.joymeter.events.bus.ActivityDeletedEvent;
import com.joymeter.events.bus.ActivityUpdatedEvent;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.service.helper.ConnectivityHelper;
import com.joymeter.utils.ShareUtils;
import com.squareup.otto.Subscribe;

/**
 * Created by hramovecchi on 15/09/2015.
 */
public class HistoryActivity extends FragmentActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        EventsBus.getInstance().register(this);

        AdviceDTO advice = (AdviceDTO)getIntent().getSerializableExtra(JoymeterPreferences.JOYMETER_ADVICE);
        if (advice != null){
            Intent i = new Intent(getApplicationContext(), SingleActivity.class);
            i.putExtra(JoymeterPreferences.JOYMETER_ADVICE,advice);
            startActivity(i);
        }

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

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe
    public void onActivityDeleted(ActivityDeletedEvent event){
        ActivityListFragment fragment = (ActivityListFragment)getFragmentManager().findFragmentById(R.id.activity_history_fragment);
        fragment.deleteActivity(event.getActivity());
    }

    @Subscribe
    public void onActivityAdded(ActivityAddedEvent event){
        ActivityListFragment fragment = (ActivityListFragment) getFragmentManager().findFragmentById(R.id.activity_history_fragment);
        fragment.addActivity(event.getActivity());
        shareOnFacebook(event.getActivity());
    }

    @Subscribe
    public void onActivityUpdated(ActivityUpdatedEvent event){
        ActivityListFragment fragment = (ActivityListFragment)getFragmentManager().findFragmentById(R.id.activity_history_fragment);
        fragment.insertActivity(event.getActivity(), event.getPosition());
        shareOnFacebook(event.getActivity());
    }

    public void shareOnFacebook(ActivityDTO activity){
        if (!activity.isClassified() && ConnectivityHelper.getHelper().hasInternetAccess()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            ShareDialog shareDialog = new ShareDialog(HistoryActivity.this);
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
                ShareContent linkContent = ShareUtils.joymeterShareLinkContent(activity);
                shareDialog.show(linkContent);
            }
        }
    }
}
