/**
 * 
 */
package com.joymeter.notification;


/**
 * @author cesarroman
 *
 */
public enum NotificationType {

//	PING(R.string.notification_user_pinged_you, R.drawable.ic_notification_bar_ping),
	SUGGEST_ACTIVITY(0, 0);
	
	private int userMessageRes;
	private int barNativeNotificationIconRes;
	
	NotificationType (int userMessageRes, int barNotificationIconRes) {
		this.userMessageRes = userMessageRes;
		this.barNativeNotificationIconRes = barNotificationIconRes;
	}
	
	public int getUserMessageRes() {
		return userMessageRes;
	}
	
	public int getBarNotificationIconRes() {
		return barNativeNotificationIconRes;
	}
}
