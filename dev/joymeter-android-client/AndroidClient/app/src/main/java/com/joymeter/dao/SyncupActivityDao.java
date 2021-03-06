package com.joymeter.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.joymeter.dao.SyncupActivity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SYNCUP_ACTIVITY".
*/
public class SyncupActivityDao extends AbstractDao<SyncupActivity, Long> {

    public static final String TABLENAME = "SYNCUP_ACTIVITY";

    /**
     * Properties of entity SyncupActivity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, String.class, "type", false, "TYPE");
        public final static Property Summary = new Property(2, String.class, "summary", false, "SUMMARY");
        public final static Property Description = new Property(3, String.class, "description", false, "DESCRIPTION");
        public final static Property Classified = new Property(4, Boolean.class, "classified", false, "CLASSIFIED");
        public final static Property LevelOfJoy = new Property(5, Integer.class, "levelOfJoy", false, "LEVEL_OF_JOY");
        public final static Property StartDate = new Property(6, Long.class, "startDate", false, "START_DATE");
        public final static Property EndDate = new Property(7, Long.class, "endDate", false, "END_DATE");
    };


    public SyncupActivityDao(DaoConfig config) {
        super(config);
    }
    
    public SyncupActivityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SYNCUP_ACTIVITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TYPE\" TEXT," + // 1: type
                "\"SUMMARY\" TEXT," + // 2: summary
                "\"DESCRIPTION\" TEXT," + // 3: description
                "\"CLASSIFIED\" INTEGER," + // 4: classified
                "\"LEVEL_OF_JOY\" INTEGER," + // 5: levelOfJoy
                "\"START_DATE\" INTEGER," + // 6: startDate
                "\"END_DATE\" INTEGER);"); // 7: endDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SYNCUP_ACTIVITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SyncupActivity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(2, type);
        }
 
        String summary = entity.getSummary();
        if (summary != null) {
            stmt.bindString(3, summary);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(4, description);
        }
 
        Boolean classified = entity.getClassified();
        if (classified != null) {
            stmt.bindLong(5, classified ? 1L: 0L);
        }
 
        Integer levelOfJoy = entity.getLevelOfJoy();
        if (levelOfJoy != null) {
            stmt.bindLong(6, levelOfJoy);
        }
 
        Long startDate = entity.getStartDate();
        if (startDate != null) {
            stmt.bindLong(7, startDate);
        }
 
        Long endDate = entity.getEndDate();
        if (endDate != null) {
            stmt.bindLong(8, endDate);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SyncupActivity readEntity(Cursor cursor, int offset) {
        SyncupActivity entity = new SyncupActivity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // summary
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // description
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // classified
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // levelOfJoy
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // startDate
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7) // endDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SyncupActivity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSummary(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setClassified(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setLevelOfJoy(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setStartDate(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setEndDate(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SyncupActivity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SyncupActivity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
