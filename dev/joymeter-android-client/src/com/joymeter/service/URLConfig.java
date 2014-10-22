/**
 * 
 */
package com.joymeter.service;

import com.joymeter.ApplicationManager;
import com.joymeter.R;

/**
 * @author cesarroman
 *
 */
public class URLConfig {

	public enum URLType {
		
		BASE_URL,
		SEND_GCM_REG_ID,
		SOCIAL_LOG_IN,
		REMOVE_GCM_REG_ID,
		CHECK_USERNAME_AVAILABILITY,
		FETCH_SERVER_TIME;
		
	}
	
	public static String fetchUrl(URLType type) {
		
		switch (type) {
		case BASE_URL:
			return ApplicationManager.getAppContext().getString(R.string.base_url);
		case SEND_GCM_REG_ID:
			return fetchUrl(URLType.BASE_URL) + ApplicationManager.getAppContext().getString(R.string.send_gcm_reg_id_url);
		case REMOVE_GCM_REG_ID:
			return fetchUrl(URLType.BASE_URL) + ApplicationManager.getAppContext().getString(R.string.remove_gcm_reg_id_url);
		case SOCIAL_LOG_IN:
			return fetchUrl(URLType.BASE_URL) + ApplicationManager.getAppContext().getString(R.string.social_log_in_url);
		case CHECK_USERNAME_AVAILABILITY:
			return fetchUrl(URLType.BASE_URL) + ApplicationManager.getAppContext().getString(R.string.check_username_availability_url);
		case FETCH_SERVER_TIME:
			return fetchUrl(URLType.BASE_URL) + ApplicationManager.getAppContext().getString(R.string.fetch_server_time_url);
		default:
			return null;
		}
		
	}
		
}
