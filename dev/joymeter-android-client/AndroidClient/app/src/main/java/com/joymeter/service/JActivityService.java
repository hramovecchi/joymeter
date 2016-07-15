package com.joymeter.service;

import com.joymeter.dao.SyncupAction;
import com.joymeter.dao.SyncupActionDao;
import com.joymeter.dao.SyncupActivity;
import com.joymeter.dao.SyncupActivityDao;
import com.joymeter.dao.UserActivity;
import com.joymeter.dao.UserActivityDao;
import com.joymeter.dto.Activities;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.SyncupActions;
import com.joymeter.events.bus.ActivitiesLoadedEvent;
import com.joymeter.events.bus.ActivityAddedEvent;
import com.joymeter.events.bus.ActivityDeletedEvent;
import com.joymeter.events.bus.ActivityUpdatedEvent;
import com.joymeter.events.bus.AddActivityEvent;
import com.joymeter.events.bus.DeleteActivityEvent;
import com.joymeter.events.bus.LoadActivitiesEvent;
import com.joymeter.events.bus.UpdateActivityEvent;
import com.joymeter.rest.ActivityService;
import com.joymeter.service.helper.ConnectivityHelper;
import com.joymeter.service.mapper.ActivityMapper;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.Query;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hramovecchi on 22/03/2016.
 */
public class JActivityService {

    public enum SyncupActionMethod{
        save, update, delete
    }

    private ActivityService api;
    private Bus bus;
    private UserActivityDao activityDao;
    private SyncupActionDao syncupActionDao;
    private SyncupActivityDao syncupActivityDao;

    public JActivityService(
            ActivityService api,
            Bus bus,
            UserActivityDao activityDao,
            SyncupActionDao syncupActionDao,
            SyncupActivityDao syncupActivityDao){

        this.api = api;
        this.bus = bus;
        this.activityDao = activityDao;
        this.syncupActionDao = syncupActionDao;
        this.syncupActivityDao = syncupActivityDao;
    }

    @Subscribe
    public void onLoadActivities(LoadActivitiesEvent event){
        //getDBActivities
        List<UserActivity> activities = activityDao.loadAll();

        //Syncup with server
        if (activities.size() == 0){
            api.getActivities(new Callback<Activities>() {
                @Override
                public void success(Activities activities, Response response) {
                    activityDao.insertInTx(ActivityMapper.mapToDB(activities.getActivities()));

                    //trigger activitiesLoadedEvent
                    bus.post(new ActivitiesLoadedEvent(activities.getActivities()));
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else {
            //trigger activitiesLoadedEvent
            bus.post(new ActivitiesLoadedEvent(ActivityMapper.mapFromDB(activities)));
        }
    }

    @Subscribe
    public void onUpdateActivity(UpdateActivityEvent event){
        event.getView().finish();
        final ActivityDTO activityToUpdate = event.getActivity();

        //updateOnDB
        activityDao.insertOrReplace(ActivityMapper.mapToDB(activityToUpdate));

        //updateOnServer or saveForLater
        if (ConnectivityHelper.getHelper().hasInternetAccess()){
            api.updateActivity(activityToUpdate.getId(), activityToUpdate, new Callback<ActivityDTO>() {
                @Override
                public void success(ActivityDTO activityDTO, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    //TODO check how syncupAction is saved
                    storeInBackupAction(activityToUpdate, SyncupActionMethod.update);
                }
            });
        } else {
            //TODO check how syncupAction is saved
            storeInBackupAction(activityToUpdate, SyncupActionMethod.update);
        }

        //trigger activityUpdatedEvent
        bus.post(new ActivityUpdatedEvent(activityToUpdate, event.getPosition()));
    }

    @Subscribe
    public void onDeleteActivity(DeleteActivityEvent event){
        final ActivityDTO activityToDelete = event.getActivity();

        //deleteOnDB
        activityDao.delete(ActivityMapper.mapToDB(activityToDelete));

        //deleteOnServer or saveForLater
        if (ConnectivityHelper.getHelper().hasInternetAccess()) {
            api.deleteActivity(activityToDelete.getId(), new ResponseCallback() {
                @Override
                public void success(Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    //TODO check how syncupAction is saved
                    storeInBackupAction(activityToDelete, SyncupActionMethod.delete);
                }
            });
        } else {
            //TODO check how syncupAction is saved
            storeInBackupAction(activityToDelete, SyncupActionMethod.delete);
        }

        //trigger activityDeletedEvent
        bus.post(new ActivityDeletedEvent(activityToDelete));
    }

    @Subscribe
    public void onAddActivity(AddActivityEvent event){
        event.getView().finish();
        final ActivityDTO activityToAdd = event.getActivity();
        activityToAdd.setId(UUID.randomUUID().getLeastSignificantBits());

        //addOnDB
        activityDao.insert(ActivityMapper.mapToDB(activityToAdd));

        //addOnServer or saveForLater
        if (ConnectivityHelper.getHelper().hasInternetAccess()){
            api.addActivity(activityToAdd, new Callback<ActivityDTO>() {
                @Override
                public void success(ActivityDTO activityDTO, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    //TODO check how syncupAction is saved
                    storeInBackupAction(activityToAdd, SyncupActionMethod.save);
                }
            });
        } else {
            //TODO check how syncupAction is saved
            storeInBackupAction(activityToAdd, SyncupActionMethod.save);
        }

        //trigger activityAddedEvent
        bus.post(new ActivityAddedEvent(activityToAdd));
    }



    public void storeInBackupAction(ActivityDTO activityToSave, SyncupActionMethod syncupActionMethod) {
        Query<SyncupAction> query = syncupActionDao.queryBuilder()
                .where(SyncupActionDao.Properties.ActivityId.eq(activityToSave.getId())).build();

        List<SyncupAction> actions = query.list();

        //new activity to store in backup action
        if (actions.size() == 0){
            SyncupActivity syncupActivity = ActivityMapper.mapToSyncupActivity(activityToSave);
            syncupActivityDao.insert(syncupActivity);

            SyncupAction syncupAction = new SyncupAction();
            syncupAction.setActivityId(syncupActivity.getId());
            syncupAction.setAction(syncupActionMethod.toString());
            syncupActionDao.insert(syncupAction);
        } else {//the activity is already present in backup action
            SyncupAction syncupAction = actions.get(0);
            switch (SyncupActionMethod.valueOf(syncupAction.getAction())){
                case save:
                    switch (syncupActionMethod){
                        case save:
                            break;
                        case update:
                            SyncupActivity activityToBackup = syncupAction.getSyncupActivity();
                            activityToBackup.setStartDate(activityToSave.getStartDate());
                            activityToBackup.setClassified(activityToSave.isClassified());
                            activityToBackup.setEndDate(activityToSave.getEndDate());
                            activityToBackup.setDescription(activityToSave.getDescription());
                            activityToBackup.setLevelOfJoy(activityToSave.getLevelOfJoy());
                            activityToBackup.setSummary(activityToSave.getSummary());
                            activityToBackup.setType(activityToSave.getType());

                            syncupActivityDao.insertOrReplace(activityToBackup);
                            break;
                        case delete:
                            syncupActionDao.delete(syncupAction);
                            break;
                    }
                    break;
                case update:
                   switch (syncupActionMethod){
                       case save:
                           break;
                       case update:
                           SyncupActivity activityToBackup = syncupAction.getSyncupActivity();
                           activityToBackup.setStartDate(activityToSave.getStartDate());
                           activityToBackup.setClassified(activityToSave.isClassified());
                           activityToBackup.setEndDate(activityToSave.getEndDate());
                           activityToBackup.setDescription(activityToSave.getDescription());
                           activityToBackup.setLevelOfJoy(activityToSave.getLevelOfJoy());
                           activityToBackup.setSummary(activityToSave.getSummary());
                           activityToBackup.setType(activityToSave.getType());

                           syncupActivityDao.insertOrReplace(activityToBackup);
                           break;
                       case delete:
                           syncupAction.setAction(SyncupActionMethod.delete.name());
                           syncupActionDao.insertOrReplace(syncupAction);
                           break;
                   }
                    break;
                case delete:
                    break;
            }
        }
    }

    public void syncupToServer() {
        if (ConnectivityHelper.getHelper().hasInternetAccess()){
            new Thread(new SyncupThread()).run();
        }
    }

    private class SyncupThread implements  Runnable{

        @Override
        public void run() {
            //getDBSyncupActivities
            List<SyncupAction> activitiesToSyncup = syncupActionDao.loadAll();

            if (activitiesToSyncup.size() > 0){
                SyncupActions syncupActions = new SyncupActions(ActivityMapper.mapToSyncupActions(activitiesToSyncup));

                //syncup with server
                api.syncupToServer(syncupActions, new ResponseCallback() {
                    @Override
                    public void success(Response response) {
                        syncupActionDao.deleteAll();
                        syncupActivityDao.deleteAll();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }
}
