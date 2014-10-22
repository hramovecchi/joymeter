/**
 * 
 */
package com.joymeter.security;

import java.util.HashSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.joymeter.ApplicationManager;
import com.joymeter.activity.LoginActivity;
import com.joymeter.exception.InvalidAuthenticationCredentialsException;
import com.joymeter.model.User;
import com.joymeter.notification.DeviceNotificationHelper;
import com.joymeter.util.SharedPreferencesUtil;
import com.joymeter.util.Util;

/**
 * @author cesarroman
 *
 */
public class AuthenticationManager {

	private static final String LOG_TAG = AuthenticationManager.class.getSimpleName();

	protected Handler handler;
	
	protected Runnable invalidateRunnable;
	
	private HashSet<SessionLifetimeListener> sessionLifetimeListeners;
	
	protected Runnable serverSynchingRunnable;
	
	private static AuthenticationManager INSTANCE;
	
	protected AuthenticationManager() {
		
		handler = new Handler();
		sessionLifetimeListeners = new HashSet<SessionLifetimeListener>();
		invalidateRunnable = new Runnable() {
			
			@Override
			public void run() {
				
				Log.d(LOG_TAG, "Reseting UserSession");
				UserSession.getInstance().reset();
				clearSessionCredentials();

				Log.d(LOG_TAG, "Unregistering from push notifications");
				// unregister from push notifications
				DeviceNotificationHelper.unregisterFromPushNotifications(false);
				DeviceNotificationHelper.cancelAllNotifications(ApplicationManager.getAppContext());

				Log.d(LOG_TAG, "Notifying session lifetime listeners that session has died");
				for (SessionLifetimeListener currentListener : getSessionLifetimeListeners())
					currentListener.onSessionDied();
				
				SharedPreferencesUtil.resetAllPreferences();
				
				if (ApplicationManager.isAppUIVisible()) {
					
					Log.d(LOG_TAG, "Redirecting to log in screen");
					redirectLogin();
					
				} else {
					Log.d(LOG_TAG, "No need to redirect to login screen because app is not in foreground");
				}
				
			}
		};
		serverSynchingRunnable = new Runnable() {
			
			@Override
			public void run() {
				
				// register for push notifications
				Log.d(LOG_TAG, "Registering for google cloud messaging push notificaitons");
				DeviceNotificationHelper.registerForPushNotifications();

				// fetch user's notifications
				Log.d(LOG_TAG, "Synching notifications with server");
//				ApplicationManager.getNotificationsManagerInstance().synchWithServer();
				
				Log.d(LOG_TAG, "Synching account info cache with server");
//				AccountInfoCache.getInstance(AuthenticationManagerProcessUI.this).synchAccountInfoWithServer();
				
			}
		};
		
	}
	
	/**
	 * It returns an instance of {@link AuthenticationManagerProcessUI}.
	 * @return
	 */
	public static AuthenticationManager getInstance() {
		
		if (INSTANCE == null) {
			
			Log.d(LOG_TAG, "Creating new instance of AuthenticationManagerProcessUI");
			INSTANCE = new AuthenticationManager();
				
			Log.d(LOG_TAG, "Performing initial authentication for new instance of AuthenticationManager in a UI process");
			INSTANCE.performInitialLocalAuthentication();
			Log.d(LOG_TAG, "Registering receiver for ui process owner");
				
		}
			
		
		return INSTANCE;
		
	}

	/**
	 * It determines if current session is authenticated.
	 * @return
	 */
	public synchronized boolean isAuthenticated(boolean forceCheckSharedPreferences) {

		boolean valid = UserSession.getInstance().isSessionValid();
		if ((!valid) && forceCheckSharedPreferences) {
			
			Log.d(LOG_TAG, "Session is not valid will configure it using local credentials");
			configureUserSessionWithLocalCredentials();
			boolean authenticated = UserSession.getInstance().isSessionValid();
			return authenticated;
			
		} else {
			Log.d(LOG_TAG, "Session is valid");
		}
		
		return valid;
		
	}
	
	/**
	 * It returns the {@link UserSession}.
	 * @return
	 */
	public synchronized UserSession getUserSession() {
		
		if (!UserSession.getInstance().isSessionValid()) {
			// this is the case where android OS has shut down JVM, and the user returns to the application
			// we need to re configure user session with local credentials
			configureUserSessionWithLocalCredentials();
		}
		
		return UserSession.getInstance();
		
	}
	
	/**
	 * It invalidates current {@link UserSession}, meaning it resets the session and broadcast session expired intent.
	 */
	public synchronized void invalidateSession() {
		
		Log.d(LOG_TAG, "Entering invalidateSession method");
		
		if (isAuthenticated(false))
			handler.post(invalidateRunnable);
		else
			Log.d(LOG_TAG, "No need to invalidate session because it is not currently authenticated");
		
		Log.d(LOG_TAG, "Leaving invalidateSession method");
		
	}
	
	/**
	 * It validates, configures and store this new {@link UserSession} with given credentials.
	 * @param serverInfo
	 * @param sessionToken
	 * @param username
	 * @param userId
	 * @param userLocationStatus
	 * @throws AuthenticationNotAllowedException
	 */
	public synchronized void validateSessionFor(
			String sessionToken,
			User user) {
		
		Log.d(LOG_TAG, "Entering validateSessionFor method");
		
		if (sessionToken == null || "".equals(sessionToken))
			throw new InvalidAuthenticationCredentialsException("Invalid session token. sessionToken= " + sessionToken == null ? "null" : sessionToken);
		if (user == null)
			throw new InvalidAuthenticationCredentialsException("Invalid authentication, user can not be null!");
		
		UserSession.getInstance().configure(sessionToken, user);
		storeSessionCredentials(sessionToken, user);
		
		new Thread(serverSynchingRunnable).start();
		
		Log.d(LOG_TAG, "Leaving validateSessionFor method");
		
	}

	/**
	 * It performs initial local authentication. If authenticated, it initializes {@link NotificationsManager}.
	 * This method should only be used as a begining authentication of a user. Once a user is authenticated, 
	 * use {@link AuthenticationManager#isAuthenticated(boolean)} in order to check if the user is authenticated or not. 
	 * @return
	 */
	boolean performInitialLocalAuthentication() {
		
		if (isAuthenticated(true)) {

			new Thread(serverSynchingRunnable).start();
			return true;

		}

		return false;
		
	}
	
	/**
	 * It refreshes credentials with local storage.
	 * Please note that current credentials will be totally <b>replaced</b> by local storage.
	 */
	public void refreshCredentials() {
	
		Log.d(LOG_TAG, "Entering refreshCredentials method");
		configureUserSessionWithLocalCredentials();
		
	}
	
	public synchronized void registerSessionLifetimeListener(SessionLifetimeListener listener) {
		
		sessionLifetimeListeners.add(listener);
		
	}
	
	public synchronized void unregisterSessionLifetimeListener(SessionLifetimeListener listener) {
		
		sessionLifetimeListeners.remove(listener);
		
	}
	
	protected synchronized HashSet<SessionLifetimeListener> getSessionLifetimeListeners() {
		return Util.cloneHashSet(sessionLifetimeListeners);
	}

	protected void redirectLogin() {
		
		Log.d(LOG_TAG, "Entering login method");
		Log.d(LOG_TAG, "Starting login activity");
		Context context = ApplicationManager.getAppContext();
		Intent intent = new Intent(context, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		Log.d(LOG_TAG, "Leaving login method");
		
	}

	/**
	 * It configures {@link UserSession} with local credentials from shared preferences.
	 */
	private synchronized void configureUserSessionWithLocalCredentials() {
		
		Context context = ApplicationManager.getAppContext();
		SharedPreferences configurations = context.getSharedPreferences(SharedPreferencesUtil.CONFIG_PREFS_NAME, Context.MODE_MULTI_PROCESS);
		String sessionToken = configurations.getString(SharedPreferencesUtil.UserSessionKeys.SESSION_TOKEN, null);
		String sessionUsername = configurations.getString(SharedPreferencesUtil.UserSessionKeys.SESSION_USERNAME, null);
		int sessionUserId = configurations.getInt(SharedPreferencesUtil.UserSessionKeys.SESSION_USER_ID, -1);
		
		User user = new User();
		user.setUsername(sessionUsername);
		user.setId(sessionUserId);
		UserSession.getInstance().configure(
				sessionToken,
				user);
		if (sessionToken != null) {
			Log.d(LOG_TAG, "sessionToken=" + sessionToken);	
		} else {
			Log.d(LOG_TAG, "sessionToken=null");
		}
		if (sessionUsername != null) {
			Log.d(LOG_TAG, "sessionUsername=" + sessionUsername);	
		} else {
			Log.d(LOG_TAG, "sessionUsername=null");
		}
		
	}
	
	/**
	 * It stores hangout session credentials into a shared preferences for later usage.
	 * @param sessionToken
	 * @param username
	 * @param userId
	 */
	protected void storeSessionCredentials(
			String sessionToken,
			User user) {

		Context context = ApplicationManager.getAppContext();
		SharedPreferences configurations = context.getSharedPreferences(SharedPreferencesUtil.CONFIG_PREFS_NAME, Context.MODE_MULTI_PROCESS);
		Editor editor = configurations.edit();
		editor.putString(SharedPreferencesUtil.UserSessionKeys.SESSION_TOKEN, sessionToken);
		editor.putString(SharedPreferencesUtil.UserSessionKeys.SESSION_USERNAME, user.getUsername());
		editor.putInt(SharedPreferencesUtil.UserSessionKeys.SESSION_USER_ID, user.getId());
		
		editor.commit();

	}

	/**
	 * It clears hangout session credentials from shared preferences.
	 */
	protected void clearSessionCredentials() {

		Context context = ApplicationManager.getAppContext();
		SharedPreferences configurations = context.getSharedPreferences(SharedPreferencesUtil.CONFIG_PREFS_NAME, Context.MODE_MULTI_PROCESS);
		Editor editor = configurations.edit();
		editor.putString(SharedPreferencesUtil.UserSessionKeys.SESSION_TOKEN, null);
		editor.putString(SharedPreferencesUtil.UserSessionKeys.SESSION_USERNAME, null);
		editor.putString(SharedPreferencesUtil.UserSessionKeys.SESSION_USER_ID, null);
		
		editor.commit();

	}

}
