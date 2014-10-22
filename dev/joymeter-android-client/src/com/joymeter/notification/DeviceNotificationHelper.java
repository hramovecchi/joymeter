/**
 * 
 */
package com.joymeter.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.joymeter.ApplicationManager;
import com.joymeter.GCMIntentService;

/**
 * @author cesarroman
 *
 */
public class DeviceNotificationHelper {
	
	private static final String LOG_TAG = "DeviceNotificationHelper";
	public static final int NOTIF_TYPE_LOCATION_SERVICE_STARTED = 1;
	public static final int NOTIF_TYPE_USER_INTERACTION = 2;
	public static final int NOTIF_TYPE_CHAT_MESSAGES = 3;

	public static void showNotification(
			NotificationType type,
			Context context, 
			int notificationId, 
			String contentTitle, 
			String contentText,
			String tickerText,
			int iconResource,
			boolean vibrate,
			boolean sound,
			boolean autocancel,
			Bitmap largeIcon,
			Class<? extends Activity> targetActivityClass,
			Bundle intentExtras) {
		
		Notification newNotification = buildNotification(
				type,
				context,
				contentTitle, 
				contentText,
				tickerText,
				iconResource,
				vibrate,
				sound,
				autocancel,
				largeIcon,
				targetActivityClass,
				intentExtras,
				notificationId);
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
			.notify(
					notificationId, 
					newNotification);
	}

	public static Notification buildNotification(
			NotificationType type,
			Context context, 
			String contentTitle, 
			String contentText,
			String tickerText,
			int iconResource,
			boolean vibrate,
			boolean sound,
			boolean autocancel,
			Bitmap largeIcon,
			Class<? extends Activity> targetActivityClass,
			Bundle intentExtras,
			int requestCode) {
		
		NotificationCompat.Builder builder = new NotificationCompat
				.Builder(context)
				.setSmallIcon(iconResource)
				.setContentTitle(contentTitle)
				.setContentText(contentText);
		
		if (vibrate){
			builder.setVibrate(new long[]{0, 500});
		}
		
		if (sound) {
			builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		}
		
		if (largeIcon != null) {
			builder.setLargeIcon(largeIcon);
		}
		
		if (tickerText != null) {
			builder.setTicker(tickerText);
		}
		
		builder.setAutoCancel(autocancel);
		
		// TODO: shouldn't we set these 2 flags: FLAG_ACTIVITY_CLEAR_TOP and FLAG_ACTIVITY_NEW_TASK ?
		Intent resultIntent = new Intent(context, targetActivityClass);
		if (intentExtras != null && !intentExtras.isEmpty()) {
			resultIntent.putExtras(intentExtras);	
		}
		
		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		// TODO : i think we don't need this
//		stackBuilder.addParentStack(HangoutActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//		        stackBuilder.getPendingIntent(
//		            0,
//		            PendingIntent.FLAG_UPDATE_CURRENT
//		        );
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            requestCode,
		            PendingIntent.FLAG_CANCEL_CURRENT
		        );
		
		builder.setContentIntent(resultPendingIntent);
		return builder.build();
		
	}
	
	/**
	 * It cancels native android notification for given {@link com.hangout.model.Notification}.
	 * @param context
	 * @param notificationToCancel
	 */
//	public static void cancelNotification(
//			Context context,
//			com.hangout.model.Notification notificationToCancel) {
//		
//		int nativeNotifId = buildNativeNotificationId(notificationToCancel);
//		cancelNotification(context, nativeNotifId);
//		
//	}

	/**
	 * It cancels native android notification for given native notification id.
	 * @param context
	 * @param nativeNotificationIdToCancel
	 */
	public static void cancelNotification(
			Context context,
			int nativeNotificationIdToCancel) {
		
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(nativeNotificationIdToCancel);
		
	}
	
	/**
	 * It cancells all native android notifications.
	 * 
	 * @param context
	 */
	public static void cancelAllNotifications(Context context) {
		
		Log.d(LOG_TAG, "Cancelling all native notifications");
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
		Log.d(LOG_TAG, "All native notifications cancelled");
		
	}

	/**
	 * It builds native notification id based on given {@link com.hangout.model.Notification}.
	 * @param notification
	 * @return
	 */
//	public static int buildNativeNotificationId(
//			com.hangout.model.Notification notification) {
//		
//		//Builds an id so all notifications of the same type for the same user use the one notification bar notification
//		return DeviceNotificationHelper.NOTIF_TYPE_USER_INTERACTION
//				+ 10 * notification.getType().ordinal()
//				+ 100 * notification.getSenderId();
//		
//	}

    /**
     * It registers this device for push notifications if not registered already.
     */
    public static void registerForPushNotifications() {
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				final Context context = ApplicationManager.getAppContext();
				// register to google for push notifications
		    	GCMRegistrar.checkDevice(context);
		    	GCMRegistrar.checkManifest(context);
		    	final String regId = GCMRegistrar.getRegistrationId(context);
		    	if ("".equals(regId)) {
		    		GCMRegistrar.register(context, GCMIntentService.SENDER_ID);
		    	} else {
		    	  Log.d(LOG_TAG, "GCM already registered");
		    	  if (GCMRegistrar.isRegisteredOnServer(context)) {
		    		  Log.d(LOG_TAG, "Already registered on server");
		    	  } else {
		    		  
		    	  }
		    	  synchWithServer(regId);
		    	}

			}
			
		}).start();
    	
    }
    
    /**
     * It unregisters this device from receiving push notifications.
     */
    public static void unregisterFromPushNotifications(final boolean unregisterFromGCMServer) {
    	
		final Context context = ApplicationManager.getAppContext();
		if (unregisterFromGCMServer) {
			
			// unregister from google push notifications
	    	GCMRegistrar.unregister(context);

		}

    	// there is no need to synch with the server because gcm registration 
    	// id for this device will be removed when session is removed
    	
    }

    /**
     * It checks if gcm configuration is synched with the server (by checking in shared preferences). 
     * If it's not synch, then it starts {@link GCMIntentService} so to do the synch with our server.
     *  
     * @param gcmRegId
     */
    private static void synchWithServer(String gcmRegId) {
    	
    	Context context = ApplicationManager.getAppContext();
		Log.d(LOG_TAG, "GCM is not synched with our server, will start service to do the synch...");
		Intent serviceIntent = new Intent(GCMIntentService.SYNCH_ACTION_NAME);
		serviceIntent.putExtra(
				GCMIntentService.GCM_REG_ID_INTENT_KEY, 
				gcmRegId);
		
		context.startService(serviceIntent);

    }
	

}
