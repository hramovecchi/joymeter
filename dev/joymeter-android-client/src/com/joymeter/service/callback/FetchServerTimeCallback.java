/**
 * 
 */
package com.joymeter.service.callback;


/**
 * @author cesarroman
 *
 */
public interface FetchServerTimeCallback {
	
	void onFetchServerTimeSuccess(long serverTime);
	
	void onError(String errorCode);
	
}
