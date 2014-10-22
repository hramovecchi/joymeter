/**
 * 
 */
package com.joymeter;

/**
 * @author cesar
 *
 */
public interface Constants {

	public static final String URL_KEY_GCM_REG_ID = "pnsId";
	
	public static class JsonKeys {
		
		public static final String JSON_ARGUMENT_NAME = "json";
		public static final String JSON_KEY_ERROR_CODE = "errorCode";
		public static final String JSON_KEY_USERNAME_AVAILABLE = "available";
		public static final String JSON_KEY_TIMESTAMP = "timestamp";
		public static final String JSON_KEY_GCM_REG_ID = "pnsClientId";
		public static final String JSON_KEY_SESSION_TOKEN = "sessionToken";
		public static final String JSON_KEY_ACCESS_TOKEN = "socialAccessToken";
		public static final String JSON_KEY_USER = "user";
		public static final String JSON_KEY_JOYMETER_USER_ID = "userId";
		public static final String JSON_KEY_USERNAME = "username";
		public static final String JSON_KEY_FULL_NAME = "fullName";
		public static final String JSON_KEY_FACEBOOK_ACCOUNT = "facebook";
		public static final String JSON_KEY_EMAIL = "email";
		public static final String JSON_KEY_SOCIAL_ACCOUNTS = "socialAccounts";
		public static final String JSON_KEY_SOCIAL_ACCOUNT_USER_ID = "userId";
		public static final String JSON_KEY_SOCIAL_ACCOUNT_USERNAME = "username";
		public static final String JSON_KEY_SOCIAL_ACCOUNT_FULLNAME = "fullName";
		
	}
	
	public enum HangoutErrors {

		UNEXPECTED,
		NETWORK_PROBLEM,
		SOCKET_TIMEOUT;
		
	}
	
}
