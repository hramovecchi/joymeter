package com.joymeter.androidclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.joymeter.dao.DaoMaster;
import com.joymeter.dao.DaoSession;
import com.joymeter.dao.SyncupActionDao;
import com.joymeter.dao.SyncupActivityDao;
import com.joymeter.dao.UserActivityDao;
import com.joymeter.events.bus.AppErrorEvent;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.rest.factory.ActivityServiceFactory;
import com.joymeter.service.JActivityService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by hramovecchi on 24/03/2016.
 */
public class JoymeterApp extends Application {

    private static JActivityService activityService;
    private static Bus eventBus;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        eventBus = EventsBus.getInstance();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "joymeter-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        UserActivityDao activityDao = daoSession.getUserActivityDao();
        SyncupActionDao syncupActionDao = daoSession.getSyncupActionDao();
        SyncupActivityDao syncupActivityDao = daoSession.getSyncupActivityDao();

        activityService = new JActivityService(ActivityServiceFactory.getInstance(), eventBus, activityDao, syncupActionDao, syncupActivityDao);
        eventBus.register(activityService);

        //listen for global events
        eventBus.register(this);

        //must be logged to do this
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sessionToken = sharedPreferences.getString(JoymeterPreferences.JOYMETER_TOKEN, null);
        if (sessionToken != null) {
            activityService.syncupToServer();
        }
    }

    @Subscribe
    public void onAppError(AppErrorEvent appErrorEvent){
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
    }

    public static JActivityService getActivityService(){
        return activityService;
    }

    public static Context getAppContext() {return context; }
}
