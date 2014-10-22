package com.joymeter.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.joymeter.Constants;
import com.joymeter.exception.RemoteServerFailedException;
import com.joymeter.model.FacebookAccount;
import com.joymeter.model.User;
import com.joymeter.network.NetworkStatusManager;
import com.joymeter.security.AuthenticationManager;
import com.joymeter.security.UserSession;
import com.joymeter.service.ServerClient.ServerCallType;
import com.joymeter.service.URLConfig.URLType;
import com.joymeter.service.callback.CheckUsernameAvailabilityCallback;
import com.joymeter.service.callback.FetchServerTimeCallback;
import com.joymeter.service.callback.LoginCallback;

/**
 * This class is in charge of performing all necessary requests to the server.
 * 
 * @author cesarroman
 *
 */
public class JoyMeterRemoteService {

	private static final String LOG_TAG = JoyMeterRemoteService.class.getSimpleName();

	private ExecutorService executorService;
	
	/**
	 * Reference to server call service.
	 */
	private ServerClient serverCallService;
	
	private AuthenticationManager authenticationManager;
	
	public JoyMeterRemoteService(AuthenticationManager authManager) {
		
		this(null, authManager);
		
	}
	
	public JoyMeterRemoteService(
			NetworkStatusManager networkStatusManager,
			AuthenticationManager authManager) {
		
		// TODO : currently initializen with http client, in the future we can use HttpUrlConnection which has more performance
		serverCallService = new ServerClient(networkStatusManager);
		authenticationManager = authManager;
		executorService = Executors.newCachedThreadPool();
		
	}

	/**
	 * It fetches the server time.
	 * @param callback
	 */
	public void fetchServerTime(
			final FetchServerTimeCallback callback) {
		
		Log.d(LOG_TAG, "Entering fetchServerTime method");
		
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				
				String fetchServerTimeUrl = URLConfig.fetchUrl(URLType.FETCH_SERVER_TIME);
				try {
					
					String response = serverCallService.sendRequest(
							fetchServerTimeUrl,
							null,
							null,
							ServerCallType.GET);
					
					handleFetchServerTimeResponse(response, callback);
					
				} catch (RemoteServerFailedException e) {
					
					// failed
					Log.e(LOG_TAG, "Remote server failed exception while doing fetch server time", e);
					if (callback != null)
						callback.onError(e.getErrorCode());
					
				} catch (JSONException e) {
					
					// json parse failed
					Log.e(LOG_TAG, "Json exception while doing fetch server time", e);
					if (callback != null)
						callback.onError(null);
					
				} catch (IOException e) {
					
					Log.e(LOG_TAG, "IOException while doing fetch server time", e);
					if (callback != null)
						callback.onError(null);
				}
				
			}
		});
		
		Log.d(LOG_TAG, "Leaving fetchServerTime method");
		
	}

	/**
	 * It performs log in to Joymeter Server. Result will be notified via {@link LoginCallback}.
	 * @param callback
	 * @param accessToken optional. Only present if it's NOT a hangout custom login
	 */
	public void login(
			final LoginCallback callback,
			final String accessToken) {
		
		Log.d(LOG_TAG, "Entering login method");
		
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				
				String loginUrl = URLConfig.fetchUrl(URLType.SOCIAL_LOG_IN);
				
				try {
					
					String response = serverCallService.sendRequest(
							loginUrl, 
							buildLoginArguments(
									accessToken),
							null,
							ServerCallType.POST);
					
					handleLoginResponse(response, callback);
					
				} catch (RemoteServerFailedException e) {
					
					// failed
					Log.e(LOG_TAG, "Remote server failed exception while doing log in", e);
					if (callback != null)
						callback.onError(e.getErrorCode());
					
				} catch (JSONException e) {
					
					// json parse failed
					Log.e(LOG_TAG, "Json exception while doing log in", e);
					if (callback != null)
						callback.onError(null);
					
				} catch (IOException e) {
					
					Log.e(LOG_TAG, "IOException while doing log in", e);
					if (callback != null)
						callback.onError(null);
				}
				
			}
		});
		
		Log.d(LOG_TAG, "Leaving login method");
		
	}

	/**
	 * It sends google cloud messaging registration id to the server.
	 * Please note there is no need for this method to be asynch.
	 * 
	 * @param gcmRegistrationId
	 * @return
	 */
	public boolean sendGCMRegistrationId(String gcmRegistrationId) {
		
		String sendGCMRegIdUrl = URLConfig.fetchUrl(URLType.SEND_GCM_REG_ID);
		try {
			
			serverCallService.sendRequest(
					sendGCMRegIdUrl, 
					buildSendGCMRegIdArguments(gcmRegistrationId),
					buildSessionTokenQueryArgument(),
					ServerCallType.PUT);
			
			return true;
			
		} catch (RemoteServerFailedException e) {
			
			// failed
			Log.e(LOG_TAG, "Remote server failed exception while doing send gcm registration id", e);
			
		} catch (JSONException e) {
			
			// json parse failed
			Log.e(LOG_TAG, "Json exception while doing send gcm registration id", e);
			
		} catch (IOException e) {
			
			Log.e(LOG_TAG, "IOException while doing send gcm registration id", e);
		}
		
		return false;
	}

	/**
	 * It tells the server to discard gcm registration id for logged user.
	 *  
	 * @param gcmRegistrationId
	 * @return
	 */
	public boolean removeGCMRegistrationId(String gcmRegistrationId) {
		
		String sendGCMRegIdUrl = URLConfig.fetchUrl(URLType.SEND_GCM_REG_ID);
		try {
			
			serverCallService.sendRequest(
					sendGCMRegIdUrl,
					null,
					buildRemoveGCMIdQueryArgument(gcmRegistrationId),
					ServerCallType.DELETE);
			
			return true;
			
		} catch (RemoteServerFailedException e) {
			
			// failed
			Log.e(LOG_TAG, "Remote server failed exception while doing remove gcm registration id", e);
			
		} catch (JSONException e) {
			
			// json parse failed
			Log.e(LOG_TAG, "Json exception while doing remove gcm registration id", e);
			
		} catch (IOException e) {
			
			Log.e(LOG_TAG, "IOException while doing remove gcm registration id", e);
		}
		
		return false;
	}

	/**
	 * It checks for username availability from Hangout Server. If username is not available to be used, 
	 * it will optionally suggest alternative usernames. Result will be notified through {@link CheckUsernameAvailabilityCallback}.
	 * @param username
	 * @param callback
	 */
	public void checkUsernameAvailability(final CheckUsernameAvailabilityCallback callback, final String username) {
		
		Log.d(LOG_TAG, "Entering checkUsernameAvailability method");
		
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				
				String checkUsernameAvailabilityUrl = URLConfig.fetchUrl(URLType.CHECK_USERNAME_AVAILABILITY) + "/" + username;
				try {
					
					String response = serverCallService.sendRequest(
							checkUsernameAvailabilityUrl, 
							ServerCallType.GET);
					
					handleCheckUsernameAvailabilityResponse(response, callback);
					
				} catch (RemoteServerFailedException e) {
					
					// failed
					Log.e(LOG_TAG, "Remote server failed exception while doing check username availability", e);
					if (callback != null)
						callback.onError(e.getErrorCode());
					
				} catch (JSONException e) {
					
					// json parse failed
					Log.e(LOG_TAG, "Json exception while doing check username availability", e);
					if (callback != null)
						callback.onError(null);
					
				} catch (IOException e) {
					
					Log.e(LOG_TAG, "IOException while doing check username availability", e);
					if (callback != null)
						callback.onError(null);
				}
				
			}
		});
		
		Log.d(LOG_TAG, "Leaving checkUsernameAvailability method");
		
	}

	/**
	 * It handles response for a sucess login. It parses the response, configures the 
	 * {@link UserSession} and notifies the callback.
	 * @param response
	 * @param callback
	 * @throws JSONException
	 */
	private void handleLoginResponse(
			String response, 
			LoginCallback callback) throws JSONException {
		
		Log.d(LOG_TAG, "Entering handleLoginResponse method");
		
		JSONObject rootJsonObject = new JSONObject(response);
		String sessionToken = rootJsonObject.optString(Constants.JsonKeys.JSON_KEY_SESSION_TOKEN);
		
		Log.d(LOG_TAG, "Parsing user info");
		User user = parseJsonToUser(rootJsonObject.optJSONObject(Constants.JsonKeys.JSON_KEY_USER));
		Log.d(LOG_TAG, "Parsing server info");
		authenticationManager.validateSessionFor(
				sessionToken, 
				user);
		Log.d(LOG_TAG, "Configuring UserSession with : sessionToken=" + sessionToken + 
				" - userId=" + user.getId() + " - username=" + user.getUsername());
		callback.onLoginSuccess();
		
		// update user info cache
		// TODO: not sure if we will use it...
//			AccountInfoCache.getInstance(authenticationManager).updateUserCache(user, System.currentTimeMillis());
			
		Log.d(LOG_TAG, "Leaving handleLoginResponse method");
		
	}
	
	/**
	 * It handles response for check username availability. If username not available, 
	 * it will try to retrieve alternative usernames from the server.
	 * @param response
	 * @param callback
	 * @throws JSONException
	 */
	private void handleCheckUsernameAvailabilityResponse(
			String response, 
			CheckUsernameAvailabilityCallback callback) throws JSONException {
		
		Log.d(LOG_TAG, "Entering handleCheckUsernameAvailabilityResponse method");
		
		JSONObject rootJsonObject = new JSONObject(response);
		boolean available = rootJsonObject.getBoolean(com.joymeter.Constants.JsonKeys.JSON_KEY_USERNAME_AVAILABLE);
		
		callback.onCheckUsernameAvailabilitySuccess(available);
		
		Log.d(LOG_TAG, "Leaving handleCheckUsernameAvailabilityResponse method");
		
	}
	
	private void handleFetchServerTimeResponse(
			String response, 
			FetchServerTimeCallback callback) throws JSONException {
		
		Log.d(LOG_TAG, "Entering handleFetchServerTimeResponse method");
		
		JSONObject rootJsonObject = new JSONObject(response);
		long serverTime = rootJsonObject.getLong(Constants.JsonKeys.JSON_KEY_TIMESTAMP);
		
		if (callback != null)
			callback.onFetchServerTimeSuccess(serverTime);
		
		Log.d(LOG_TAG, "Leaving handleFetchServerTimeResponse method");
		
	}
	

	/**
	 * It builds arguments map for send gcm registration id web service.
	 * @param gcmRegistrationId
	 * @return
	 * @throws JSONException
	 */
	private Map<String, String> buildSendGCMRegIdArguments(String gcmRegistrationId) throws JSONException {
		
		Log.d(LOG_TAG, "Parsing aguments for send gcm registration id");
		Log.d(LOG_TAG, Constants.JsonKeys.JSON_KEY_GCM_REG_ID + "=" + gcmRegistrationId);
		
		Map<String, String> arguments = new HashMap<String, String>();
		
		JSONObject jsonArgument = new JSONObject();
		jsonArgument.put(Constants.JsonKeys.JSON_KEY_GCM_REG_ID, gcmRegistrationId);
		
		// add json argument to the map
		arguments.put(Constants.JsonKeys.JSON_ARGUMENT_NAME, jsonArgument.toString());

		return arguments;
		
	}
	
	/**
	 * It builds arguments map for login with a social bridge.
	 * @param loginType
	 * @param accessToken
	 * @param usernameOrEmail
	 * @param password
	 * @return
	 */
	private Map<String, String> buildLoginArguments(
			String accessToken) throws JSONException {
		
		Log.d(LOG_TAG, "Parsing aguments for login");
		Map<String, String> arguments = new HashMap<String, String>();
		
		JSONObject jsonArgument = new JSONObject();
		jsonArgument.put(Constants.JsonKeys.JSON_KEY_ACCESS_TOKEN, accessToken);
		
		// add json argument to the map
		arguments.put(Constants.JsonKeys.JSON_ARGUMENT_NAME, jsonArgument.toString());
		
		return arguments;
		
	}
	
	private Map<String, String> buildSessionTokenQueryArgument() throws JSONException {
		
		String hangoutSessionToken = authenticationManager.getUserSession().getSessionToken();
		Log.d(LOG_TAG, "Parsing aguments for build session token");
		Log.d(LOG_TAG, Constants.JsonKeys.JSON_KEY_SESSION_TOKEN + "=" + hangoutSessionToken);
		
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(Constants.JsonKeys.JSON_KEY_SESSION_TOKEN, hangoutSessionToken);
		
		return arguments;
		
	}

	private Map<String, String> buildRemoveGCMIdQueryArgument(String gcmId) throws JSONException {
		
		String hangoutSessionToken = authenticationManager.getUserSession().getSessionToken();
		Log.d(LOG_TAG, "Parsing aguments for build session token");
		Log.d(LOG_TAG, Constants.JsonKeys.JSON_KEY_SESSION_TOKEN + "=" + hangoutSessionToken);
		
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(Constants.JsonKeys.JSON_KEY_SESSION_TOKEN, hangoutSessionToken);
		arguments.put(Constants.URL_KEY_GCM_REG_ID, gcmId);
		
		return arguments;
		
	}
		
	public ServerClient getServerCallService() {
		
		return serverCallService;
		
	}
	
	public NetworkStatusManager getNetworkStatusManager() {
		
		return serverCallService.getNetworkStatusManager();
		
	}

	/**
	 * It parses a json object representing a user into a {@link User}.
	 * @param userJsonObject
	 * @return
	 * @throws JSONException
	 */
	private User parseJsonToUser(JSONObject userJsonObject) 
				throws JSONException {
		
		if (userJsonObject == null) {
			return null;
		} else {
			
			User user = new User();
			user.setId(Integer.parseInt(userJsonObject.optString(Constants.JsonKeys.JSON_KEY_JOYMETER_USER_ID)));
			
			// optional values
			user.setUsername(userJsonObject.optString(Constants.JsonKeys.JSON_KEY_USERNAME));
			user.setFullName(userJsonObject.optString(Constants.JsonKeys.JSON_KEY_FULL_NAME));
			user.setFacebookAccountId(userJsonObject.optString(Constants.JsonKeys.JSON_KEY_FACEBOOK_ACCOUNT, null));
			user.setEmail(userJsonObject.optString(Constants.JsonKeys.JSON_KEY_EMAIL, null));
			
			// social accounts
			JSONObject socialAccountsJson = userJsonObject.optJSONObject(Constants.JsonKeys.JSON_KEY_SOCIAL_ACCOUNTS);
			if (socialAccountsJson != null) {
				
				// TODO: parse this! could we instead of send facebook: {}, twitter: {} send something generic with type=facebook or twitter?
				Log.d(LOG_TAG, "Parsing social accounts");
				JSONObject facebookJson = socialAccountsJson.optJSONObject(Constants.JsonKeys.JSON_KEY_FACEBOOK_ACCOUNT);
				if (facebookJson != null) {
					
					Log.d(LOG_TAG, "Parsing facebook account");
					FacebookAccount facebookAccount = new FacebookAccount();
					// mandatory
					facebookAccount.setId(facebookJson.getString(Constants.JsonKeys.JSON_KEY_SOCIAL_ACCOUNT_USER_ID));
					// optionals
					facebookAccount.setUsername(facebookJson.optString(Constants.JsonKeys.JSON_KEY_SOCIAL_ACCOUNT_USERNAME, null));
					facebookAccount.setFullName(facebookJson.optString(Constants.JsonKeys.JSON_KEY_SOCIAL_ACCOUNT_FULLNAME, null));
					user.addSocialAccount(facebookAccount);
					
				} else
					Log.d(LOG_TAG, "No facebook account to parse");

			} else
				Log.d(LOG_TAG, "No social accounts to parse");
			

			return user;

		}
		
	}
	
}
