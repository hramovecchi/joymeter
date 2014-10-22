/**
 * 
 */
package com.joymeter.service.callback;

import java.util.List;

/**
 * @author cesarroman
 *
 */
public interface CheckUsernameAvailabilityCallback {
	
	void onCheckUsernameAvailabilitySuccess(boolean available);
	
	void onError(String errorCode);
	
}
