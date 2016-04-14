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
import com.joymeter.events.bus.ActivityAddedEvent;
import com.joymeter.events.bus.AddActivityEvent;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.events.bus.UpdateActivityEvent;
import com.joymeter.utils.ShareUtils;
import com.squareup.otto.Subscribe;


public class SingleActivity extends FragmentActivity {

    private enum SaveAction{
        save, update
    }

    private SaveAction saveAction = SaveAction.save;
    private boolean notificationCall = Boolean.FALSE;
    private CallbackManager callbackManager;

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
                    EventsBus.getInstance().post(new AddActivityEvent(SingleActivity.this, activity));
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

    @Override
    protected void onDestroy() {
        if (notificationCall) {
            EventsBus.getInstance().unregister(this);
        }
        super.onDestroy();
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe
    public void onActivityAdded(ActivityAddedEvent event){
        ActivityListFragment fragment = (ActivityListFragment) getFragmentManager().findFragmentById(R.id.activity_history_fragment);
        fragment.addActivity(event.getActivity());
        shareOnFacebook(event.getActivity());
    }

    public void shareOnFacebook(ActivityDTO activity){
        if (!activity.isClassified()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            ShareDialog shareDialog = new ShareDialog(SingleActivity.this);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(FacebookException error) {
                }
            });

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareContent linkContent = ShareUtils.joymeterShareLinkContent(activity);
                shareDialog.show(linkContent);
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
