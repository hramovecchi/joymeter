/**
 * 
 */
package com.joymeter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.joymeter.ApplicationManager;


/**
 * @author cesar.m.roman@gmail.com
 *
 */
public class SharedPreferencesUtil {

	private static final String LOG_TAG = SharedPreferencesUtil.class.getSimpleName();
	
	public static final String CONFIG_PREFS_NAME = "configurations";

	public static final String GCM_REG_ID = "gcm-reg-id";
	
	public static class UserSessionKeys {
		
		// username of logged user
		public static String SESSION_USERNAME = "session-username";
		// user id of logged user
		public static String SESSION_USER_ID = "session-user-id";
		// session token of logged user
		public static String SESSION_TOKEN = "session-token";
		
	}
	
	/**
	 * This method resets all shared preferences.
	 * 
	 * Warning: please be sure when calling this method, because all preferences 
	 * will be lost and app may stop working correctly.
	 * An example of where this method should be called from is when logging out.
	 * 
	 */
	public static void resetAllPreferences() {
	
		Log.d(LOG_TAG, "About to reset all shared preferences");
		
		SharedPreferences configurations = ApplicationManager.getAppContext().getSharedPreferences(SharedPreferencesUtil.CONFIG_PREFS_NAME, Context.MODE_MULTI_PROCESS);
		Editor editor = configurations.edit();	
		editor.clear();
		editor.commit();
		
	}
	
}
