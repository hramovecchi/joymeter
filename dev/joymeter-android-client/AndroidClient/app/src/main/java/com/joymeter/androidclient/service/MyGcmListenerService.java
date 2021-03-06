package com.joymeter.androidclient.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.joymeter.androidclient.HistoryActivity;
import com.joymeter.androidclient.JoymeterPreferences;
import com.joymeter.androidclient.R;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.AdviceDTO;

/**
 * Created by hramovecchi on 02/12/2015.
 */
public class MyGcmListenerService extends GcmListenerService{

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Gson gson = new Gson();
        //TODO must handle advice DTO here
        AdviceDTO advice = gson.fromJson(data.getString("advice"), AdviceDTO.class);

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(advice);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param activity GCM message received.
     */
    private void sendNotification(AdviceDTO advice) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra(JoymeterPreferences.JOYMETER_ADVICE,advice);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String contentText = advice.getSuggestedActivity().getDescription().length() < 25 ?
                advice.getSuggestedActivity().getDescription() :
                advice.getSuggestedActivity().getDescription().substring(0, 24) + "...";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_jm_launcher)
                .setContentTitle("Joymeter Recommendation")
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
