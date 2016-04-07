package com.joymeter.dao;

import com.joymeter.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SYNCUP_ACTION".
 */
public class SyncupAction {

    private Long id;
    private long activityId;
    private String action;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient SyncupActionDao myDao;

    private SyncupActivity syncupActivity;
    private Long syncupActivity__resolvedKey;


    public SyncupAction() {
    }

    public SyncupAction(Long id) {
        this.id = id;
    }

    public SyncupAction(Long id, long activityId, String action) {
        this.id = id;
        this.activityId = activityId;
        this.action = action;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSyncupActionDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    /** To-one relationship, resolved on first access. */
    public SyncupActivity getSyncupActivity() {
        long __key = this.activityId;
        if (syncupActivity__resolvedKey == null || !syncupActivity__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SyncupActivityDao targetDao = daoSession.getSyncupActivityDao();
            SyncupActivity syncupActivityNew = targetDao.load(__key);
            synchronized (this) {
                syncupActivity = syncupActivityNew;
            	syncupActivity__resolvedKey = __key;
            }
        }
        return syncupActivity;
    }

    public void setSyncupActivity(SyncupActivity syncupActivity) {
        if (syncupActivity == null) {
            throw new DaoException("To-one property 'activityId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.syncupActivity = syncupActivity;
            activityId = syncupActivity.getId();
            syncupActivity__resolvedKey = activityId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}