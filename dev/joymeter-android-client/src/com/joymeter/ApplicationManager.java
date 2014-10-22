/**
 * 
 */
package com.joymeter;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.joymeter.network.NetworkStatusManager;
import com.joymeter.security.AuthenticationManager;
import com.joymeter.service.JoyMeterRemoteService;

/**
 * This class is intended to be used as a static way to obtain the Application's 
 * context and any global service.
 * @author Cesar Roman
 *
 */
public final class ApplicationManager extends Application {
	
	private static final String LOG_TAG = ApplicationManager.class.getSimpleName();
	
	/**
	 * Application's context.
	 */
	private static Context appContext;
	
	private static Handler handler;
	
	private static JoyMeterRemoteService remoteServiceInstance;
	private static NetworkStatusManager networkStatusManagerInstance;
	private static AuthenticationManager authenticationManagerInstance;

	/**
	 * Override for onCreate method. It will just fetch the 
	 * application context from its parent.
	 */
	@Override
	public void onCreate() {
		
		Log.d(LOG_TAG, "Entering onCreate method");
		super.onCreate();
		appContext = getApplicationContext();
		handler = new Handler();
		
		Log.d(LOG_TAG, "Leaving onCreate method");
	}

	/**
	 * It returns the application's context.
	 * @return application context
	 */
	public static Context getAppContext() {
		
		return appContext;
		
	}
	
	/**
	 * It returns a {@link Handler} that has been initialized in a the ui thread.
	 * @return
	 */
	public static Handler getUIHandler() {
		
		return handler;
		
	}
	
	/**
	 * It determines if <b>UI process is in foreground</b> and if screen is turned on.
	 * @return
	 */
	public static boolean isAppUIVisible() {
		
		Log.d(LOG_TAG, "Determining if app is in foreground");
		PowerManager pm = (PowerManager) getAppContext().getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		boolean isProcessForeground = false;
		Context context = ApplicationManager.getAppContext();
	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    if (appProcesses == null) {
	    	Log.d(LOG_TAG, "App is not in foreground");
	    	isProcessForeground = false;
	    }
	    
	    final String packageName = context.getPackageName();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	      // TODO: should we check for IMPORTANCE_VISIBLE too?
		  if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
			  Log.d(LOG_TAG, "App is in foreground");
			  isProcessForeground = true;
		  }
	      
	    }
	    
	    
	    Log.d(LOG_TAG, "App is not in foreground");
	    return isScreenOn && isProcessForeground;
	    
	}	
	
	public static int getAPILevel() {
	
		return android.os.Build.VERSION.SDK_INT;
		
	}
	
	/**
	 * It hides soft keyboard if available.
	 * @param activity
	 */
    public static void hideSoftKeyboard(Activity activity) {
    	InputMethodManager imm = (InputMethodManager) activity.getSystemService(
    		    Context.INPUT_METHOD_SERVICE);
    	if (imm != null) {
    		View currentFocus = activity.getCurrentFocus();
    		if (currentFocus != null) {
    			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);	
    		}
    	}
    }

    /**
     * It vibrates the device for a short period of time.
     */
    public static void vibrateShortlyDevice() {
    	
    	Vibrator v = (Vibrator) getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
    	if (v != null) {
    		v.vibrate(250);
    	}
    	
    }
    
	public static NetworkStatusManager getNetworkStatusManagerInstance() {
		
		if (networkStatusManagerInstance == null) {
			
			networkStatusManagerInstance = NetworkStatusManager.getInstance();
			
		}
		
		return networkStatusManagerInstance;
		
	}

	public static AuthenticationManager getAuthenticationManagerInstance() {
		
		if (authenticationManagerInstance == null) {
			
			authenticationManagerInstance = AuthenticationManager.getInstance();
			
		}
		
		return authenticationManagerInstance;
		
	}

	public static JoyMeterRemoteService getRemoteServiceInstance() {
		
		if (remoteServiceInstance == null) {
			
			remoteServiceInstance = new JoyMeterRemoteService(
					getNetworkStatusManagerInstance(), 
					getAuthenticationManagerInstance());
			// set remote service to network status manager
			getNetworkStatusManagerInstance().setRemoteService(remoteServiceInstance);
			
		}
		
		return remoteServiceInstance;
		
	}
   
}