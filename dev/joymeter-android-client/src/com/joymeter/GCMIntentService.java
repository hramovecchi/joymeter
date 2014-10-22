/**
 * 
 */
package com.joymeter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.joymeter.notification.NotificationType;
import com.joymeter.security.AuthenticationManager;
import com.joymeter.util.SharedPreferencesUtil;

/**
 * @author cesarroman
 *
 */
// TODO : please check if moving this class to a more proper package still works or not
// because i'm not sure if according to Android this should be at root package
public class GCMIntentService extends GCMBaseIntentService {

	private static final String LOG_TAG = "GCMIntentService";
	
	public static final String GCM_REG_ID_INTENT_KEY = "intent-reg-id";
	public static final String GCM_NOTIFICATION_ID_INTENT_KEY = "id";
	public static final String GCM_NOTIFICATION_TYPE_INTENT_KEY = "type";

	public static final String SYNCH_ACTION_NAME = "com.hangout.intent.SYNCH";
	private static final int MAX_RETRY = 20;
	public static final String SENDER_ID;
	static {
		SENDER_ID = ApplicationManager.getAppContext().getString(R.string.project_number);
	}
	
	private AuthenticationManager authenticationManager;
	
	private volatile boolean gcmSynched = false;
	// if true, then it means it is currently in the process of synching
	private volatile boolean gcmSynching = false;
	private volatile boolean gcmRemoveSynching = false;
	private volatile int gcmRemoveSyncRetries = 0;
	private volatile int gcmUpdateSyncRetries = 0;

	private long syncGCMStartTime;
	private long syncGCMRemoveStartTime;
	// If fails wait 200ms and retry
	private static final long SYNC_GCM_RETRY_SLEEP_1 = 200;

	// After 5 seconds of retrying increase the wait time to 2 seconds
	private static final long SYNC_GCM_RETRY_TIME_TO_2 = 5 * 1000;
	private static final long SYNC_GCM_RETRY_SLEEP_2 = 2 * 1000;

	// After 40 seconds of retrying increase the wait time to 30 seconds
	private static final long SYNC_GCM_RETRY_TIME_TO_3 = 40 * 1000;
	private static final long SYNC_GCM_RETRY_SLEEP_3 = 30 * 1000;

	ExecutorService executorService;
	
//	private HangoutRemoteService remoteService;
	
	public GCMIntentService() {
		
		super(SENDER_ID);
		executorService = Executors.newCachedThreadPool();
//		remoteService = ApplicationManager.getRemoteServiceInstance();
		authenticationManager = AuthenticationManager.getInstance();
		
	}

	/**
	 * It is overridden so to check if we are trying to perform a server synch up. 
	 * This is a custom flow, not GCM's flow.
	 * This is done so to reuse same functionality as when we register but when 
	 * we need to synch up with our server. 
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (SYNCH_ACTION_NAME.equals(intent.getAction())) {
			
			Log.d(LOG_TAG, "Received intent to do a GCM with our server synch up");
			String regId = intent.getExtras().getString(GCM_REG_ID_INTENT_KEY);
			storeGCMRegistrationId(regId);
			doSynchUp(this, regId);
			return Service.START_NOT_STICKY;
			
		}
		
		return super.onStartCommand(intent, flags, startId);
		
	}


	/**
	 * Called when the device tried to register/unregister but 
	 * the GCM returned error.
	 */
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO : do something here
		String error = arg1;
		String e2 = error;
	}

	/**
	 * Called when you receive a message from GCM server 
	 * (indirectly from our Server).
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {

		if (intent.hasExtra(GCM_NOTIFICATION_ID_INTENT_KEY)) {
			
			NotificationType type = NotificationType.valueOf(intent.getStringExtra(GCM_NOTIFICATION_TYPE_INTENT_KEY));
			String notificationId = intent.getStringExtra(GCM_NOTIFICATION_ID_INTENT_KEY);
			if (notificationId != null) {

				// TODO: define what to do here
//				Log.d(LOG_TAG, "Received push notification with notificationId=" + notificationId);
//				NotificationsManager notificationManager = ApplicationManager.getNotificationsManagerInstance();
//				if (notificationManager != null) {
//					Log.d(LOG_TAG, "Notifying notification manager to synch with server");
//					notificationManager.synchWithServer();
//				} else
//					Log.w(LOG_TAG, "Unable to notify notification manager to synch with server because notification manager is NULL!");
					
				
			}

		}
		
	}

	/**
	 * Called after registration intent is received.
	 * It stores gcm registration id into a shared preferences and tries 
	 * to send it to the server. If for any reason it is not available to 
	 * send this registration id to the server, it will retry later.
	 */
	@Override
	protected void onRegistered(Context context, String regId) {

		storeGCMRegistrationId(regId);
		doSynchUp(context, regId);
		
	}

	/**
	 * Called after the device has been unregistered from gcm.
	 * It removes gcm registration id from shared preferences and 
	 * tells the server to discard such information for this user.
	 */
	@Override
	protected void onUnregistered(final Context context, final String regId) {
	}
	
	/**
	 * It performs a gcm registration id synch up.
	 * It first stores it in this device's shared preferences and then it 
	 * proceeds to synch up with our server.
	 * 
	 * @param gcmRegId
	 */
	private void doSynchUp(final Context context, final String gcmRegId) {

		boolean synch;
		synchronized(this) {
			if (!gcmSynching) {
				synch = true;
				gcmSynching = true;
				syncGCMStartTime = SystemClock.elapsedRealtime();
			} else {
				synch = false;
			}
		}
		if (synch) {

			if (authenticationManager.isAuthenticated(false) && gcmUpdateSyncRetries < MAX_RETRY) {

				executorService.execute(new Runnable() {
					
					@Override
					public void run() {
						
//						if (!remoteService.sendGCMRegistrationId(gcmRegId)) {
//
//							long sleep;
//							synchronized(GCMIntentService.this) {
//								gcmSynching = false;
//								gcmSynched = false;
//								gcmUpdateSyncRetries++;
//								
//								sleep = SYNC_GCM_RETRY_SLEEP_1;
//								long processingTime =  SystemClock.elapsedRealtime() - syncGCMStartTime;
//								if (processingTime > SYNC_GCM_RETRY_TIME_TO_3) {
//									sleep = SYNC_GCM_RETRY_SLEEP_3;
//								} else if (processingTime > SYNC_GCM_RETRY_TIME_TO_2) {
//									sleep = SYNC_GCM_RETRY_SLEEP_2;
//								}
//
//							}
//							
//							try {
//								Log.d(LOG_TAG, "Sync friends failed - waiting "
//										+ sleep + " ms before retrying");
//								Thread.sleep(sleep);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							
//							doSynchUp(context, gcmRegId);
//
//						} else {
//							
//							Log.d(LOG_TAG, "Successfully synched GCM id with server");
//							GCMRegistrar.setRegisteredOnServer(context, true);
//							
//							synchronized(GCMIntentService.this) {
//								gcmSynching = false;
//								gcmSynched = true;
//								syncGCMStartTime = SystemClock.elapsedRealtime();
//								gcmUpdateSyncRetries = 0;
//							}
//
//						}
						
					}
						
				});

			} else {
				synchronized(GCMIntentService.this) {
					gcmSynching = false;
					gcmSynched = false;
					gcmUpdateSyncRetries = 0;
				}
			}

		} else
			Log.d(LOG_TAG, "Will NOT synch GCM registration with server because it is currently synching");

	}
	
	private void storeGCMRegistrationId(String regId) {
		
		SharedPreferences preferences = ApplicationManager.getAppContext().getSharedPreferences(SharedPreferencesUtil.CONFIG_PREFS_NAME, Context.MODE_PRIVATE);
		preferences.edit()
		.putString(SharedPreferencesUtil.GCM_REG_ID, regId)
		.commit();
		
	}
	
}
