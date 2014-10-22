package com.joymeter.service.http;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * This class implements web service call with apache <code>HttpClient</code>.
 * @author cesarroman
 *
 */
public class HttpClientWrapper {

	private static final String LOG_TAG = HttpClientWrapper.class.getSimpleName();
	
	/**
	 * It performs an http POST call. 
	 * @param url
	 * @param jsonBodyArguments
	 * @param queryArguments
	 * @return
	 * @throws IOException
	 */
	public HttpResponse doPOST(String url, String jsonBodyArguments, Map<String, String> queryArguments)
			throws IOException {
		
		HttpClient httpclient = getDefaultHttpClient();
		url = appendParameters(url, queryArguments);
		Log.d(LOG_TAG, "Executing a POST with url=" + url);
		
		HttpPost httpPost = new HttpPost(url);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
		if (jsonBodyArguments != null && !"".equals(jsonBodyArguments)) {
			
			StringEntity stringEntity = new StringEntity(jsonBodyArguments, "UTF-8");
			stringEntity.setContentType(basicHeader);
			httpPost.setEntity(stringEntity);
			
		} else
			httpPost.setHeader(basicHeader);
		
		httpPost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		
		// execute http post request
	    return httpclient.execute(httpPost);

	}
	
	/**
	 * It performs an http put call.
	 * @param url
	 * @param jsonBodyArguments
	 * @param queryArguments
	 * @return
	 * @throws IOException
	 */
	public HttpResponse doPUT(String url, String jsonBodyArguments, Map<String, String> queryArguments)
			throws IOException {
		
		HttpClient httpclient = getDefaultHttpClient();
		url = appendParameters(url, queryArguments);
		Log.d(LOG_TAG, "Executing a PUT with url=" + url);
		
		HttpPut httpPut = new HttpPut(url);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
		if (jsonBodyArguments != null && !"".equals(jsonBodyArguments)) {
			
			StringEntity stringEntity = new StringEntity(jsonBodyArguments, "UTF-8");
			stringEntity.setContentType(basicHeader);
			httpPut.setEntity(stringEntity);
			
		} else
			httpPut.setHeader(basicHeader);
		
		// execute http put request
	    return httpclient.execute(httpPut);

	}

	/**
	 * It performs an http get call.
	 * @param url
	 * @param arguments
	 * @return
	 * @throws IOException
	 */
	public HttpResponse doGET(String url, Map<String, String> arguments)
			throws IOException {

		HttpClient httpclient;
		httpclient = getDefaultHttpClient();
		
		url = appendParameters(url, arguments);
		Log.d(LOG_TAG, "Executing a GET with url=" + url);
		
		HttpGet httpget = new HttpGet(url);
		
		// execute http get request
	    return httpclient.execute(httpget);

	}
	
	/**
	 * It performs an http delete call.
	 * @param url
	 * @param arguments
	 * @return
	 * @throws IOException
	 */
	public HttpResponse doDELETE(String url, Map<String, String> arguments)
			throws IOException {
		
		HttpClient httpclient = getDefaultHttpClient();
		url = appendParameters(url, arguments);
		Log.d(LOG_TAG, "Executing a DELETE with url=" + url);
		
		HttpDelete httpdelete = new HttpDelete(url);
		
		// execute http delete request
	    return httpclient.execute(httpdelete);

	}
	
	/**
	 * It builds a default {@link HttpClient} with time out parameters from config file.
	 * @return
	 */
	private HttpClient getDefaultHttpClient() {
		
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
		return new DefaultHttpClient(httpParameters);
		
	}

	/**
	 * It appends parameters to the url.
	 * @param url
	 * @param arguments
	 * @return
	 */
	private String appendParameters(String url, Map<String, String> arguments) {
		
		if (arguments != null && !arguments.isEmpty()) {
			
			if (!url.endsWith("?")) {
				url += "?";
			}
			
			List<NameValuePair> params = new LinkedList<NameValuePair>();
			for (Entry<String, String> currentEntry : arguments.entrySet()) {
				params.add(new BasicNameValuePair(currentEntry.getKey(), currentEntry.getValue()));
			}
			
			String paramsString = URLEncodedUtils.format(params, "utf-8");
			url += paramsString;
			
		}
		
		return url;
		
	}

}
