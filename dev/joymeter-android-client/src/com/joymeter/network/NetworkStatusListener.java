/**
 * 
 */
package com.joymeter.network;

/**
 * @author cesarroman
 *
 */
public interface NetworkStatusListener {
	
	void onLostNetworkConnection();
	
	void onRestablishedNetworkConnection();

}
