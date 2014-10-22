/**
 * 
 */
package com.joymeter.service.callback;


/**
 * @author cesarroman
 *
 */
public interface LoginCallback {

	void onLoginSuccess();
	
	void onError(String errorCode);
	
}
