/**
 * 
 */
package com.joymeter.network;

import java.util.HashSet;

import android.os.Handler;
import android.util.Log;

import com.joymeter.service.JoyMeterRemoteService;
import com.joymeter.util.Util;

/**
 * @author cesarroman
 *
 */
public class NetworkStatusManager {
	
	private static final String LOG_TAG = NetworkStatusManager.class.getSimpleName();
	
	private static final long CHECK_INTERVAL_THRESHOLD = 40 * 1000;
	// time to wait when network manager first initialized
	private static final long INITIAL_WAIT_LOCK_THRESHOLD = 10 * 1000;
	private static final long NOT_STARTED = -1;
	
	/**
	 * Time interval threshold to wake up at.
	 */
	private static final long INACTIVITY_PING_INTERVAL_THRESHOLD = 15 * 1000;
	
	private static NetworkStatusManager INSTANCE;
	
	private LatestRequestStatusHolder requestsHolder;
	
	private HashSet<NetworkStatusListener> listeners;
	
	private NetworkStatusEvent lastNetworkEvent;
	
	private JoyMeterRemoteService remoteService;
	
	private Handler handler;
	// runnable instance that will ping the server every certain period of time
	private Runnable inactivityNetworkChecker;
	// flag indicating if we are currently listening
	private volatile boolean listening = false;
	private long startedTime = NOT_STARTED;
	private int failureRequests, successRequests;

	/**
	 * Network status events that listener will listen to.
	 * @author cesarroman
	 *
	 */
	public enum NetworkStatusEvent {
		
		LOST_NETWORK_CONNECTION,
		
		NETWORK_CONNECTION_RESTABLISHED;
		
	}
	
	private NetworkStatusManager() {
		
		requestsHolder = new LatestRequestStatusHolder(CHECK_INTERVAL_THRESHOLD);
		listeners = new HashSet<NetworkStatusListener>();
		handler = new Handler();
		inactivityNetworkChecker = new Runnable() {
			
			@Override
			public void run() {

				Log.d(LOG_TAG, "Will try to ping server");
				pingServerAsynch();
				postInactivityCheckerRunnable();
				
			}
			
		};
		
	}
	
	public synchronized void reset() {
		
		requestsHolder.reset();
		listeners.clear();
		lastNetworkEvent = null;
		
	}
	
	public static NetworkStatusManager getInstance() {
		
		if (INSTANCE == null)
			INSTANCE = new NetworkStatusManager();
		
		return INSTANCE;
		
	}
	
	public void logSuccessRequest(long timestamp) {
		
		Log.d(LOG_TAG, "Entering logSuccessRequest method");
		
		requestsHolder.addSuccessRequest(timestamp);
		Log.d(LOG_TAG, "Will check network status");
		checkNetworkStatus();
		
		Log.d(LOG_TAG, "Leaving logSuccessRequest method");
		
	}

	public void logFailureRequest(long timestamp) {
		
		Log.d(LOG_TAG, "Entering logFailureRequest method");
		
		requestsHolder.addFailureRequest(timestamp);
		Log.d(LOG_TAG, "Will check network status");
		checkNetworkStatus();
		
		Log.d(LOG_TAG, "Leaving logFailureRequest method");
		
	}
	
	/**
	 * It starts listening for network events.
	 * @param listener
	 * @param notifyPreviousLostNetwork if true and network is currently lost, listener will get notified
	 */
	public synchronized void startListening(NetworkStatusListener listener, boolean notifyPreviousLostNetwork) {

		Log.d(LOG_TAG, "Entering startListening method");
		
		if (!listening) {
			
			startedTime = System.currentTimeMillis();
			listening = true;
			
		}
		
		registerListener(listener);
		if (notifyPreviousLostNetwork) {
			if (NetworkStatusEvent.LOST_NETWORK_CONNECTION.equals(lastNetworkEvent)) {
				// currently lost network, notify only this listener
				listener.onLostNetworkConnection();
			}
		}
		
		Log.d(LOG_TAG, "Leaving startListening method");

	}
	
	public synchronized void startListening(NetworkStatusListener listener) {
		
		startListening(listener, false);
		
	}
	
	public synchronized void stopListening(NetworkStatusListener listener) {
		
		Log.d(LOG_TAG, "Entering stopListening method");

		unregisterListener(listener);

		if (listening) {
			if (listeners.isEmpty()) {
				// no other listeners
				listening = false;

			}
			
		}
		
		Log.d(LOG_TAG, "Leaving stoptListening method");
		
	}
	
	private synchronized void registerListener(NetworkStatusListener listener) {
		
		if (listeners.add(listener)) {
			Log.d(LOG_TAG, "Registering listener");
		} else {
			Log.d(LOG_TAG, "Listener already registered");
		}
		
	}
	
	private synchronized void unregisterListener(NetworkStatusListener listener) {
		
		listeners.remove(listener);
		
	}
	
	public void setRemoteService(JoyMeterRemoteService remoteService) {
		
		this.remoteService = remoteService;
		
	}

	private synchronized void notifyEvent(NetworkStatusEvent event) {
		
		Log.d(LOG_TAG, "Entering notify method");

		lastNetworkEvent = event;
		for (NetworkStatusListener currentListener : Util.cloneHashSet(listeners)) {

			switch (event) {
			case LOST_NETWORK_CONNECTION:
				Log.d(LOG_TAG, "Notifying listener for : "
						+ NetworkStatusEvent.LOST_NETWORK_CONNECTION.name()
						+ " event");
				currentListener.onLostNetworkConnection();
				break;
			case NETWORK_CONNECTION_RESTABLISHED:
				Log.d(LOG_TAG,
						"Notifying listener for : "
								+ NetworkStatusEvent.NETWORK_CONNECTION_RESTABLISHED
										.name() + " event");
				currentListener.onRestablishedNetworkConnection();
				break;
			default:
				break;

			}

		}

		Log.d(LOG_TAG, "Leaving notify method");
		
	}
	
	private void postInactivityCheckerRunnable() {
		
		Log.d(LOG_TAG, "Will try to start inactivity checker runnable");
		handler.postDelayed(inactivityNetworkChecker, INACTIVITY_PING_INTERVAL_THRESHOLD);
		
	}
	
	private void stopInactivityChecker() {
		
		Log.d(LOG_TAG, "Will try to stop inactivity checker runnable");
		handler.removeCallbacks(inactivityNetworkChecker);
		
	}
	
	private void checkNetworkStatus() {
		
		boolean locked = startedTime == NOT_STARTED || !(System.currentTimeMillis() - startedTime > INITIAL_WAIT_LOCK_THRESHOLD);
		if (!locked) {
			
			failureRequests = requestsHolder.getFailureRequests().size();
			successRequests = requestsHolder.getSuccessRequests().size();
			
			Log.d(LOG_TAG, "failure requests=" + failureRequests + " - success requests=" + successRequests);
			if (failureRequests == 0 && successRequests == 0) {
				
				// unknown
				pingServerAsynch();
				
			} else if ((failureRequests > 0 && successRequests > 0) || (failureRequests == 0 && successRequests > 0)) {
				
				// poor or good connection
				if (NetworkStatusEvent.LOST_NETWORK_CONNECTION.equals(lastNetworkEvent)) {
					notifyEvent(NetworkStatusEvent.NETWORK_CONNECTION_RESTABLISHED);
					stopInactivityChecker();
				}
					
				
			} else if (failureRequests > 0 && successRequests == 0) {
				
				// no connection
				if (!NetworkStatusEvent.LOST_NETWORK_CONNECTION.equals(lastNetworkEvent)) {
					notifyEvent(NetworkStatusEvent.LOST_NETWORK_CONNECTION);
					postInactivityCheckerRunnable();
				}
					
			}
			
		} else
			Log.d(LOG_TAG, "Unable to check because we are still locked");
		
	}
	
	/**
	 * It pings the server so to determine if we have network connection or not. This method is asynch.
	 * By ping we mean accessing any server api, in this case we are pinging <b>fetchServerTime</b> api.
	 */
	private void pingServerAsynch() {
		
		Log.d(LOG_TAG, "Entering pingServer method");
		boolean isActive = true;
		synchronized (this) {
			isActive = listening;
		}
		if (isActive) {

			// ping the server so network status manager logs success or failure
			// TODO: uncomment when implemented
//			remoteService.fetchServerTime(new FetchServerTimeCallback() {
//				
//				@Override
//				public void onFetchServerTimeSuccess(long serverTime) {
//					// no need to do anything
//				}
//				
//				@Override
//				public void onError(String errorCode) {
//					// no need to do anything
//					Log.e(LOG_TAG, "Failed to ping server - errorCode=" + errorCode);
//				}
//			});

		}
		
		Log.d(LOG_TAG, "Leaving pingServer method");
		
	}
	
}
