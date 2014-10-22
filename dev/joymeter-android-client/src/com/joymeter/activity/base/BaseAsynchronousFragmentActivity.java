/**
 * 
 */
package com.joymeter.activity.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

/**
 * @author cesar.m.roman@gmail.com
 *
 */
public class BaseAsynchronousFragmentActivity extends FragmentActivity {
	
	private Handler uiHandler;
	
	@Override
	protected void onStart() {
		
		super.onStart();
		uiHandler = new Handler();
		
	}
	
	@Override
	protected void onStop() {
		
		synchronized (this) {
			// remove any pending callbacks and messages 
			uiHandler.removeCallbacksAndMessages(null);
			uiHandler = null;
		}
		
		super.onStop();
		
	}
	
	/**
	 * Any asynchronous response that wishes to update any component of the UI must neccesarilly be executed through this method.
	 * Just pass a {@link Runnable} of what needs to be executed and we can be sure that if 
	 * this {@link Activity} has been brought to background then this runnablable will not be executed 
	 * and ui will be consistent.
	 * 
	 * @param runnable to be executed
	 */
	protected synchronized void executeSafeInUI(Runnable runnable) {
		
		if (uiHandler != null) {
			uiHandler.post(runnable);
		}
		
	}
	
	protected Handler getUIHandler() {
		
		if (uiHandler == null) {
			uiHandler = new Handler(Looper.getMainLooper());
		}
		
		return uiHandler;
		
	}

}
