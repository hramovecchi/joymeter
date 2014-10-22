package com.joymeter.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.joymeter.Constants;
import com.joymeter.exception.RemoteServerFailedException;
import com.joymeter.network.NetworkStatusManager;
import com.joymeter.service.http.HttpClientWrapper;

/**
 * This class acts as a bridge to the {@link HttpClientWrapper} class.
 * It is in charge of performing POST, PUT, GET and DELETE http calls 
 * with or without body/query arguments.
 * 
 * @author cesarroman
 *
 */
public class ServerClient {

	private static final String LOG_TAG = "ServerClient";

	private NetworkStatusManager networkStatusManager;
	
	private HttpClientWrapper httpClient;
	
	public enum ServerCallType {
	
		POST,
		PUT,
		GET,
		DELETE;
		
	}
	
	public ServerClient(NetworkStatusManager theNetworkStatusManager) {
		
		networkStatusManager = theNetworkStatusManager;
		httpClient = new HttpClientWrapper();
		
	}

	public String sendRequest(
			String url, 
			Map<String, String> bodyArguments, 
			Map<String, String> queryArguments, 
			ServerCallType callType) 
			throws RemoteServerFailedException, JSONException, IOException {
		
		return sendRequest(url, bodyArguments, queryArguments, callType, false);
		
	}

	public String sendRequest(
			String url, 
			Map<String, String> bodyArguments, 
			Map<String, String> queryArguments, 
			ServerCallType callType,
			boolean chatHttpClient) 
			throws RemoteServerFailedException, JSONException, IOException {

//		Log.d(LOG_TAG, "Entering sendRequest method");
		
		try {

			HttpResponse response = null;
			switch (callType) {
			case POST:
				response = httpClient.doPOST(
						url, 
						bodyArguments == null ? null : bodyArguments.get(Constants.JsonKeys.JSON_ARGUMENT_NAME), 
						queryArguments);
				break;
			case PUT:
				response = httpClient.doPUT(
						url, 
						bodyArguments == null ? null : bodyArguments.get(Constants.JsonKeys.JSON_ARGUMENT_NAME), 
						queryArguments);
				break;
			case GET:
				response = httpClient.doGET(url, queryArguments);
				break;
			case DELETE:
				response = httpClient.doDELETE(url, queryArguments);
				break;
			default:
				break;
			}
			
			if (response != null) {
				
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
					
					Log.d(LOG_TAG, "Response successfull");
					// http code 200
					if (networkStatusManager != null)
						networkStatusManager.logSuccessRequest(System.currentTimeMillis());
					
			    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			    	String responseString = null;
			    	HttpEntity entity = response.getEntity();
			    	if (entity != null) {

				        response.getEntity().writeTo(out);
				        out.close();
				        responseString = out.toString();
				        Log.d(LOG_TAG, "Response=" + responseString);

			    	}			    	
			        return responseString;

				} else {
					
					Log.d(LOG_TAG, "Response unsuccessfull");
					// no success
					if (networkStatusManager != null)
						networkStatusManager.logFailureRequest(System.currentTimeMillis());

			    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			    	String responseString = null;
			    	HttpEntity entity = response.getEntity();
			    	if (entity != null) {

				        response.getEntity().writeTo(out);
				        out.close();
				        responseString = out.toString();
				        Log.d(LOG_TAG, "Response=" + responseString);

			    	}			    	
			        
			        RemoteServerFailedException exception = new RemoteServerFailedException("Response was not success.\nResponse : " + response.toString());
			        if (responseString != null) {

				        JSONObject rootJsonObject = new JSONObject(responseString);
				        String errorCode = rootJsonObject.optString(Constants.JsonKeys.JSON_KEY_ERROR_CODE, null);
						exception.setErrorCode(errorCode);
						Log.e(LOG_TAG, "Response is ERROR with errorCode=" + errorCode);
				        
			        }

			        throw exception;
		
				}
				
			} else
				throw new RuntimeException("Http response is null !");

		} catch (SocketTimeoutException e) {
			
			Log.d(LOG_TAG, "Socket timeout exception");
			RemoteServerFailedException ex = new RemoteServerFailedException(e.getMessage(), e);
			ex.setErrorCode(Constants.HangoutErrors.SOCKET_TIMEOUT.name());
			throw ex;
			
		} catch (IOException e) {

			// no success
			if (networkStatusManager != null)
				networkStatusManager.logFailureRequest(System.currentTimeMillis());
			
			RemoteServerFailedException ex = new RemoteServerFailedException(e.getMessage(), e);
			ex.setErrorCode(Constants.HangoutErrors.NETWORK_PROBLEM.name());
			throw ex;
			
		}
			
	}
	
	public String sendRequest(String url, ServerCallType callType) 
			throws RemoteServerFailedException, JSONException, IOException {
		
		return sendRequest(url, null, null, callType);
		
	}
	
	public void setNetworkStatusManager(NetworkStatusManager theNetworkStatusManager) {
		
		networkStatusManager = theNetworkStatusManager;
		
	}
	
	public NetworkStatusManager getNetworkStatusManager() {
		
		return networkStatusManager;
		
	}

}
