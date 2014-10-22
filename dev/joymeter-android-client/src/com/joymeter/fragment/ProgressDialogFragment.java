/**
 * 
 */
package com.joymeter.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author cesarroman
 *
 */
public class ProgressDialogFragment extends DialogFragment {

	public static ProgressDialogFragment newInstance(String message) {
		
		ProgressDialogFragment frag = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;

	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String message = getArguments().getString("message");
		final ProgressDialog dialog = new ProgressDialog(getActivity());
	    dialog.setMessage(message);
	    dialog.setIndeterminate(true);
	    dialog.setCancelable(false);
	    dialog.setCanceledOnTouchOutside(false);
	    return dialog;
	    
	}
	
}
