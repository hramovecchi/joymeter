/**
 * 
 */
package com.joymeter.model;

import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author cesarroman
 *
 */
public class FacebookAccount implements Parcelable {
	
	private String id;
	private String username;
	private String fullName;
	private List<String> permissions;
	
	public static final Parcelable.Creator<FacebookAccount> CREATOR = new Parcelable.Creator<FacebookAccount>() {
		public FacebookAccount createFromParcel(Parcel in) {
			return new FacebookAccount(in);
		}

		public FacebookAccount[] newArray(int size) {
			return new FacebookAccount[size];
		}
	};

	public FacebookAccount() {}
	
	public FacebookAccount(FacebookAccount accountToCopy) {
		id = accountToCopy.id;
		username = accountToCopy.username;
		fullName = accountToCopy.fullName;
		if (accountToCopy.permissions != null && !accountToCopy.permissions.isEmpty()) {
			List<String> newPermissions = new LinkedList<String>();
			for (String permissinoToAdd : accountToCopy.permissions) {
				newPermissions.add(permissinoToAdd);
			}
			accountToCopy.permissions = newPermissions;
		}
	}
	
	public FacebookAccount(Parcel src) {
		
		id = src.readString();
		username = src.readString();
		fullName = src.readString();
		if (src.readByte() == 1) {
			permissions = new LinkedList<String>();
			src.readStringList(permissions);
		}
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(id);
		dest.writeString(username);
		dest.writeString(fullName);
		if (permissions != null && !permissions.isEmpty()) {
			dest.writeByte((byte) 1);
			dest.writeStringList(permissions);
		} else {
			dest.writeByte((byte) 0);
		}
		
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
}
