/**
 * 
 */
package com.joymeter.security;

import java.util.HashMap;
import java.util.Map;

import com.joymeter.model.User;

/**
 * It holds data related to the user session. Please note it is not possible to have 2 user 
 * sessions at the same time, this is why this class is static.
 * 
 * Important note : in order to access to this session from a different <bold>PROCESS</bold>, 
 * then you need first need to call {@link AuthenticationManager#isAuthenticated()} and then 
 * your {@link UserSession} will be able to be used if available.
 *  
 * @author cesarroman
 *
 */
public class UserSession {//implements AccountInfoCacheListener {

	private static final String LOG_TAG = UserSession.class.getSimpleName();
	
	private static UserSession instance;
	
	private AuthenticationManager authManager;
//	private AccountInfoCache accountInfoCache;
	
	private String SESSION_TOKEN;
	private User loggedUser;
	
	private UserSession() {
	
		authManager = AuthenticationManager.getInstance();
//		accountInfoCache = AccountInfoCache.getInstance(authManager);
		
	}
	
	/**
	 * This map contains parameters that the application may store for later use.
	 * Please note, these parameters will be lost if operative system destroy this 
	 * variable in case it needs memory space.
	 */
	private static Map<String, Object> SESSION_SCOPE_PARAMETERS;
	
	static UserSession getInstance() {
		
		if (instance == null) {
			instance = new UserSession();
		}
		
		return instance;
		
	}
	
//	@Override
//	public void onAccountInfoSynched() {
//
//		Log.d(LOG_TAG, "Account info synched call");
//		User localCacheUser;
//		try {
//			localCacheUser = accountInfoCache.fetchCacheUser();
//			if (loggedUser.equals(localCacheUser)) {
//				
//				// this is so to get all fresh data of the user from server. Note that status is determined by the session not the server  
//				LocationStatus validCurrentStatus = loggedUser.getLocationStatus(); 
//				loggedUser = localCacheUser;
//				loggedUser.setLocationStatus(validCurrentStatus);
//				
//			}
//			
//		} catch (AccountInfoNotSynchedException e) {
//			Log.w(LOG_TAG, "Received account info synched call but it is not actually synched!");
//		}
//	
//	}
	
	/**
	 * It configures current session with given credentials.
	 * @param sessionToken
	 * @param username
	 * @param userId
	 * @param userMapStatus
	 */
	synchronized UserSession configure(
			String sessionToken,
			User user) {
		
		SESSION_TOKEN = sessionToken;
		loggedUser = new User(user);
//		accountInfoCache.registerListener(this);
		
		return instance;
		
	}
	
	/**
	 * It resets current session.
	 */
	synchronized void reset() {

		if (instance != null) {

//			accountInfoCache.unregisterListener(this);
			instance = null;

		}
		
	}
	
	/**
	 * It determines if Hangout session is valid.
	 * @return
	 */
	public synchronized boolean isSessionValid() {
		return (SESSION_TOKEN != null && !"".equals(SESSION_TOKEN));
	}
	
	/**
	 * It returns current valid session token.
	 * @return
	 */
	public String getSessionToken() {
		return SESSION_TOKEN;
	}
	
	/**
	 * It returns logged {@link User}.
	 * @return
	 */
	public synchronized User getLoggedUser() {
		return loggedUser;
	}
	
	/**
	 * It temporary stores given parameter into memory.
	 * @param key
	 * @param value
	 */
	public void temporaryStoreSessionParameter(String key, Object value) {
		
		if (SESSION_SCOPE_PARAMETERS == null)
			SESSION_SCOPE_PARAMETERS = new HashMap<String, Object>();
		
		SESSION_SCOPE_PARAMETERS.put(key, value);
		
	}
	
	/**
	 * It fetches session scope parameter for given key if available.
	 * @param key
	 * @return
	 */
	public <T> T fetchParameter(String key) {
		
		if (SESSION_SCOPE_PARAMETERS == null || !SESSION_SCOPE_PARAMETERS.containsKey(key))
			return null;
		
		return (T) SESSION_SCOPE_PARAMETERS.get(key);
		
	}
	
	/**
	 * It determines if local parameter for session exists for given key.
	 * @param key
	 * @return
	 */
	public boolean containsLocalParameter(String key) {
		
		return (SESSION_SCOPE_PARAMETERS != null && SESSION_SCOPE_PARAMETERS.containsKey(key));
		
	}
	
	public void removeLocalParameter(String key) {
		
		if (SESSION_SCOPE_PARAMETERS != null) {
			SESSION_SCOPE_PARAMETERS.remove(key);
		}
		
	}
	
}
