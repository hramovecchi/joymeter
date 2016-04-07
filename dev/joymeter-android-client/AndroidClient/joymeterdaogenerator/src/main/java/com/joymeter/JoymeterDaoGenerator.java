package com.joymeter;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.DaoGenerator;

public class JoymeterDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1,"com.joymeter.dao");
        createDatabaseModel(schema);

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }

    private static void createDatabaseModel(Schema schema) {
        Entity activity = schema.addEntity("UserActivity");
        activity.addIdProperty();
        activity.addStringProperty("type");
        activity.addStringProperty("summary");
        activity.addStringProperty("description");
        activity.addBooleanProperty("classified");
        activity.addIntProperty("levelOfJoy");
        activity.addLongProperty("startDate");
        activity.addLongProperty("endDate");

        Entity syncupActivity = schema.addEntity("SyncupActivity");
        syncupActivity.addIdProperty();
        syncupActivity.addStringProperty("type");
        syncupActivity.addStringProperty("summary");
        syncupActivity.addStringProperty("description");
        syncupActivity.addBooleanProperty("classified");
        syncupActivity.addIntProperty("levelOfJoy");
        syncupActivity.addLongProperty("startDate");
        syncupActivity.addLongProperty("endDate");

        Entity syncupAction = schema.addEntity("SyncupAction");
        syncupAction.addIdProperty();
        Property activityId = syncupAction.addLongProperty("activityId").notNull().getProperty();
        syncupAction.addToOne(syncupActivity, activityId);
        syncupAction.addStringProperty("action");
    }
}
