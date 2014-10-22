/**
 * 
 */
package com.joymeter.view;

import android.view.View;

/**
 * Holder class for an invalid field containing its view and its message.
 * @author cesarroman
 *
 */
public class InvalidFieldHolder {

	private View view;
	private int errorMessageResource;
	
	public InvalidFieldHolder(View view, int errorMessage) {
		this.view = view;
		this.errorMessageResource = errorMessage;
	}

	public View getView() {
		return view;
	}

	public int getErrorMessageResource() {
		return errorMessageResource;
	}

}
