/**
 * 
 */
package com.joymeter.activity;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.joymeter.ApplicationManager;
import com.joymeter.R;
import com.joymeter.activity.base.BaseAsynchronousFragmentActivity;
import com.joymeter.fragment.ProgressDialogFragment;
import com.joymeter.security.AuthenticationManager;
import com.joymeter.service.JoyMeterRemoteService;
import com.joymeter.service.callback.LoginCallback;
import com.joymeter.view.InvalidFieldHolder;

/**
 * @author cesarroman
 *
 */
public class LoginActivity extends BaseAsynchronousFragmentActivity {

	private static final String LOG_TAG =LoginActivity.class.getSimpleName();
	
	private static final String TAG_PROGRESS_DIALOG_FRAGMENT = "tag-progress-dialog-fragment";
	
	public static final String ARG_AUTOMATIC_FACEBOOK_SIGNUP = "automatic-facebook-signup";
	
	private static final String LOG_IN_FACEBOOK_CALLBACK_SERIAL = "log-in-facebook";
	private static final String FETCH_OWNER_FACEBOOK_CALLBACK_SERIAL = "fetch-owner-facebook";
	
//	private FacebookBridge facebookBridge;
//	private BridgeDispatcher bridgeDispatcher;
//	private BridgeObserver FACEBOOK_BRIDGE_OBSERVER;
	
	private AuthenticationManager authenticationManager;
	
	private ExecutorService executorService;
	
	private ProgressDialogFragment progressDialog = null;
	
	private JoyMeterRemoteService hangoutService;
	
	// TODO: i think we won't have username/password login
//	private EditText usernameInput, passwordInput;
	
	private static LoginCallback facebookLoginCallback;
	
	private Runnable loginRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		Log.d(LOG_TAG, "Entering onCreate method");
		
		super.onCreate(savedInstanceState);
		// TODO: put log in layout
		setContentView(R.layout.login_screen);
		
		authenticationManager = AuthenticationManager.getInstance();
		executorService = Executors.newCachedThreadPool();
		hangoutService = ApplicationManager.getRemoteServiceInstance();
//		facebookBridge = new FacebookBridge();
//		bridgeDispatcher = BridgeDispatcher.getInstance();
//		FACEBOOK_BRIDGE_OBSERVER = new BridgeObserver() {
//			
//			@Override
//			public void onBridgeEvent(
//					final String callbackSerial,
//					final BridgeEvent event,
//					final BridgeEventResult eventResult,
//					final Map<String, Object> extras) {
//			
//				Log.d(LOG_TAG, "Entering onBridgeEvent for event : " + event.name());
//				switch (event) {
//				case CONNECT:
//					if (BridgeEventResult.SUCCESS.equals(eventResult)) {
//							showProgressDialog();
//						if (LOG_IN_FACEBOOK_CALLBACK_SERIAL.equals(callbackSerial)) {
//							performGlanceoutLogin();
//						}
//						
//					} else {
//						executeSafeInUI(new Runnable() {
//							
//							@Override
//							public void run() {
//								
//								Log.d(LOG_TAG, "Error while connecting to Facebook.\nError message : " + extras.get(Constants.BRIDGE_ERROR_MESSAGE_KEY));
//								dismissProgressDialog();
//								Toast.makeText(
//										LoginActivity.this, 
//										R.string.toast_facebook_login_error, 
//										Toast.LENGTH_SHORT)
//											.show();
//								
//							}
//						});
//						Log.d(LOG_TAG, "Tracking login with facebook failed to google analytics");
//						Map<AnalyticArgumentType, String[]> analyticArgs = new HashMap<AnalyticArgumentType, String[]>();
//						analyticArgs.put(AnalyticArgumentType.LABEL, new String[]{(String) extras.get(Constants.BRIDGE_ERROR_MESSAGE_KEY)});
//						analyticTracker.trackEvent(Category.FORMS, Action.SUBMIT_FAILED, Label.LOGIN_FACEBOOK_ERROR, analyticArgs);
//
//					}
//					break;
//				default:
//					break;
//				}
//				
//			}
//			
////				@Override
////				public String getIdentification() {
////					return LoginActivity.this.getClass().getSimpleName();
////				}
//		};

		facebookLoginCallback = new FacebookLoginCallback();
		
		loginRunnable = new Runnable() {
			
			@Override
			public void run() {
				
				final InvalidFieldHolder invalidFieldHolder = validateFields();
				if (invalidFieldHolder != null) {
					executeSafeInUI(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(
									LoginActivity.this, 
									invalidFieldHolder.getErrorMessageResource(), 
									Toast.LENGTH_SHORT).show();
					        invalidFieldHolder.getView().requestFocus();
							
						}
					});
					
				} else {
					showProgressDialog();
					// login to hangout with user / pass
//					hangoutService.login(
//							regularLoginCallback, 
//							null, 
//							usernameInput.getText().toString(), 
//							passwordInput.getText().toString());
					
				}
				
			}
		};
		
		initializeUIComponents();
		
		Log.d(LOG_TAG, "Leaving onCreate method");
		
	}
	
	protected void dismissProgressDialog() {
		
		executeSafeInUI(new Runnable() {
			
			@Override
			public void run() {
				if (progressDialog != null && progressDialog.isAdded()) {
					progressDialog.dismiss();
				}
			}
			
		});

	}

	protected void showProgressDialog() {
		
		executeSafeInUI(new Runnable() {
			
			@Override
			public void run() {

				if (progressDialog == null) {
					progressDialog = ProgressDialogFragment.newInstance(
							getResources().getString(R.string.loading));
				}
				
				if (!progressDialog.isAdded()) {
					progressDialog.show(getSupportFragmentManager(), TAG_PROGRESS_DIALOG_FRAGMENT);	
				}

			}
			
		});

	}
	
//	private void performGlanceoutLogin() {
//		// facebook authenticated
//		hangoutService.login(
//				facebookLoginCallback, 
//				AccessType.FACEBOOK, 
//				facebookBridge.getActiveSession().getAccessToken(),
//				null,
//				null);
//	}
	
	@Override
	protected void onDestroy() {
//		FACEBOOK_BRIDGE_OBSERVER = null;
		executorService.shutdown();
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		if (authenticationManager.isAuthenticated(true)) {
			
			Log.d(LOG_TAG, "Session is authenticated");
			Log.d(LOG_TAG, "Will synch account info cache");
//			AccountInfoCache.getInstance(authenticationManager).synchAccountInfoWithServer();
			trasspass();
			return;
			
		}
		
//		bridgeDispatcher.registerForBridge(
//				BridgeType.FACEBOOK, 
//				FACEBOOK_BRIDGE_OBSERVER);
		
	}
	
	@Override
	protected void onStop() {
		
//		bridgeDispatcher.unregisterFromBridge(
//				BridgeType.FACEBOOK, 
//				FACEBOOK_BRIDGE_OBSERVER);
		super.onStop();
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
//		facebookBridge.saveSessionTo(outState);
		super.onSaveInstanceState(outState);
		
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		// this is mandatory by facebook
//		facebookBridge.onActivityResult(this, requestCode, resultCode, data);
		
	}

	public void onLoginFacebookClicked(View view) {
		
		ApplicationManager.hideSoftKeyboard(this);
		showProgressDialog();
		
		Log.d(LOG_TAG, "Connecting to Facebook");
//		facebookBridge.login(LOG_IN_FACEBOOK_CALLBACK_SERIAL, this, null, FacebookBridge.READ_PERMISSIONS, FacebookBridge.PUBLISH_PERMISSIONS);
		
	}
	
	public void onLoginClicked(View view) {
		
		ApplicationManager.hideSoftKeyboard(this);
		
		Log.d(LOG_TAG, "Logging into Hangout");
		executorService.execute(loginRunnable);
		
	}
	
	private void initializeUIComponents() {
		
//		usernameInput = (EditText) findViewById(R.id.login_username);
//		passwordInput = (EditText) findViewById(R.id.login_password);
		
	}
	
	protected void trasspass() {
		
//		Log.d(LOG_TAG, "Redirecting to MainActivity");
//		Intent originalIntent = getIntent();
//		Intent intent = new Intent(this, MainActivity.class);  
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		
//		manageIntentExtras(intent, originalIntent);
//		
//		startActivity(intent);
//		dismissProgressDialog();
//		finish();
		executeSafeInUI(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(LoginActivity.this, "yes, you can trasspass!", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private void manageIntentExtras(Intent intent, Intent originalIntent) {

		Bundle extras = originalIntent.getExtras();
		if (extras != null) {
			// pass extras to next intent
			intent.putExtras(extras);
			
			// TODO: i am not sure we should remove ALL extras... we should remove only keys that we customized
			removeIntentExtras(originalIntent, extras);
			setIntent(originalIntent);
		}

	}
	
	// TODO: this is not working, when starting this activity again, old extras are still there
	private void removeIntentExtras(Intent originalIntent, Bundle extras) {
		
		// reset original intent extras
		Iterator<String> it = extras.keySet().iterator();
		while (it.hasNext()) {
			String extraKey = it.next();
			originalIntent.removeExtra(extraKey);
		}

	}

//	private void redirectToSignUp(Map<String, Serializable> args) {
//		
//		Log.d(LOG_TAG, "Redirecting to SignUpActivity");
//		Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//		if (args != null && !args.isEmpty()) {
//			Bundle bundle = new Bundle();
//			for (Entry<String, Serializable> currentEntry : args.entrySet()) {
//				bundle.putSerializable(currentEntry.getKey(), currentEntry.getValue());
//			}
//			intent.putExtras(bundle);
//		}
//		
//		startActivityForResult(intent, Constants.SIGNUP_REQUEST_CODE);
//
//	}
	
	/**
	 * It validates if all fields are valid.
	 * @return
	 */
	private InvalidFieldHolder validateFields() {
		
//		if ("".equals(usernameInput.getText().toString()))
//			return new InvalidFieldHolder(usernameInput, R.string.toast_login_username_empty_error);
//		if ("".equals(passwordInput.getText().toString()))
//			return new InvalidFieldHolder(passwordInput, R.string.toast_login_password_empty_error);

		return null;
		
	}

	/**
	 * Regular login callback class.
	 * @author cesarroman
	 *
	 */
	private class RegularLoginCallback implements LoginCallback {
		
		@Override
		public void onLoginSuccess() {
			trasspass();
		}
		
		@Override
		public void onError(String errorCode) {

			dismissProgressDialog();
//			if (Constants.HangoutErrors.SOCIAL_USER_NOT_LINKED.name().equals(errorCode)) {
//
//				executeSafeInUI(new Runnable() {
//
//					@Override
//					public void run() {
//
//						DialogFragment dialog = SignupRedirectDialog.newInstance();
//						dialog.show(getSupportFragmentManager(), "signup-redirect-fragment-tag");
//
//					}
//					
//				});
//				
//			} else {
//				ServerErrorUtility.showErrorFor(LoginActivity.this, errorCode);
//			}
			
		}
		
	}
	
	/**
	 * Facebook login callback class.
	 * @author cesarroman
	 *
	 */
	private class FacebookLoginCallback extends RegularLoginCallback {
	}

//	public static class SignupRedirectDialog extends DialogFragment {
//
//		private LoginActivity loginActivity;
//		
//		public static SignupRedirectDialog newInstance() {
//			
//			SignupRedirectDialog frag = new SignupRedirectDialog();
//	        return frag;
//	        
//	    }
//
//		@Override
//		public void onAttach(Activity activity) {
//			
//			super.onAttach(activity);
//			loginActivity = (LoginActivity) activity;
//			
//		}
//		
//	    @Override
//	    public Dialog onCreateDialog(Bundle savedInstanceState) {
//	    	
//	        return new AlertDialog.Builder(getActivity())
//	                .setMessage(getString(R.string.pop_up_signup_redirect_message))
//	                .setPositiveButton(R.string.create_button,
//	                    new DialogInterface.OnClickListener() {
//	                        public void onClick(DialogInterface dialog, int whichButton) {
//	                        	
//	    						// social user not linked, redirect to sign up
//	    						Map<String, Serializable> args = new HashMap<String, Serializable>();
//	    						args.put(ARG_AUTOMATIC_FACEBOOK_SIGNUP, true);
//	    						loginActivity.redirectToSignUp(args);
//	                        	dismiss();
//	                        	
//	                        }
//	                    }
//	                )
//	                .setNegativeButton(android.R.string.cancel,
//	                    new DialogInterface.OnClickListener() {
//	                        public void onClick(DialogInterface dialog, int whichButton) {
//	                        	
//	                        	dismiss();
//	                        	
//	                        }
//	                    }
//	                )
//	                .create();
//	    		
//	    }
//
//	}

}