package com.joymeter.androidclient;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
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

    @Override
    public void onCreate() {
        super.onCreate();

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

        //TODO must be logged to do this
        //activityService.syncupToServer();
    }

    @Subscribe
    public void onAppError(AppErrorEvent appErrorEvent){
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
    }

    public static JActivityService getActivityService(){
        return activityService;
    }
}
