/**
 * 
 */
package com.joymeter.network;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

/**
 * It holds latest request status for success and failures. It keeps both lists updated so to contain 
 * only for <b>CHECK_INTERVAL_THRESHOLD</b> period of time.
 * @author cesarroman
 *
 */
public class LatestRequestStatusHolder {
	
	private static final String LOG_TAG = "LatestRequestStatusHolder";
	
	private List<Long> latestSuccessRequest;
	private List<Long> latestFailureRequest;
	private long checkIntervalThreshold;
	
	public LatestRequestStatusHolder(long theCheckIntervalThreshold) {
		
		checkIntervalThreshold = theCheckIntervalThreshold;
		
	}
	
	public void addSuccessRequest(long timestamp) {
	
		Log.d(LOG_TAG, "Entering addSuccessRequest method");
		
		addRequest(timestamp, getSuccessRequests());
		checkRequests(timestamp, getFailureRequests());
		
		Log.d(LOG_TAG, "Leaving addSuccessRequest method");
		
	}
	
	public void addFailureRequest(long timestamp) {
		
		Log.d(LOG_TAG, "Entering addFailureRequest method");
		
		addRequest(timestamp, getFailureRequests());
		checkRequests(timestamp, getSuccessRequests());
		
		Log.d(LOG_TAG, "Leaving addFailureRequest method");
		
	}

	private synchronized void addRequest(long timestamp, List<Long> requests) {
		
		Log.d(LOG_TAG, "Leaving addLocation method");
		
		requests.add(timestamp);
		Collections.sort(requests);
		checkRequests(timestamp, requests);
		
		Log.d(LOG_TAG, "Leaving addLocation method");
		
	}
	
	private synchronized void checkRequests(long timestamp, List<Long> requests) {
		
		while (requests.size() >= 1 && timestamp - requests.get(0) >= checkIntervalThreshold) {
			requests.remove(0);
		}

	}
	
	public synchronized List<Long> getSuccessRequests() {
		
		if (latestSuccessRequest == null)
			latestSuccessRequest = new LinkedList<Long>();
		
		return latestSuccessRequest;
		
	}

	public synchronized List<Long> getFailureRequests() {
		
		if (latestFailureRequest == null)
			latestFailureRequest = new LinkedList<Long>();
		
		return latestFailureRequest;
		
	}
	
	/**
	 * It determines if there has been inactivity for more than timeThreshold.
	 * @param timeThreshold
	 * @return
	 */
	public synchronized boolean inactivityFor(long timeThreshold) {
		
		long currentTime = System.currentTimeMillis();
		List<Long> successReq = getSuccessRequests();
		if (successReq.isEmpty() || (currentTime - successReq.get(successReq.size() - 1) > timeThreshold))
			return true;
		List<Long> failureReq = getFailureRequests();
		if (failureReq.isEmpty() || (currentTime - failureReq.get(failureReq.size() - 1) > timeThreshold))
			return true;
		
		return false;
		
	}
	
	public synchronized void reset() {
		
		getSuccessRequests().clear();
		getFailureRequests().clear();
		
	}

}
