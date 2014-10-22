package com.joymeter.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.joymeter.util.ParcelUtils;


/**
 * @author cesarroman
 * 
 */
public class User implements Parcelable {
	
	public static final int NO_AVATAR = -1;
	
	private int id;
	
	private String username;
	
//	private Date dateOfBirth;
	
	private List<FacebookAccount> socialAccounts;
	
	private String email;
	
	private String fullName;
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public User() {}

	public User(User userToCopy) {
	
		id = userToCopy.id;
		username = userToCopy.username;
		if (userToCopy.socialAccounts != null && !userToCopy.socialAccounts.isEmpty()) {
			List<FacebookAccount> accounts = new LinkedList<FacebookAccount>();
			for (FacebookAccount currentAccount : userToCopy.socialAccounts) {
				FacebookAccount newAccount = new FacebookAccount(currentAccount);
				accounts.add(newAccount);
			}
			socialAccounts = accounts;
		}
		email = userToCopy.email;
		fullName = userToCopy.fullName;
		
	}

	public User(Parcel src) {
		
		id = src.readInt();
		username = src.readString();
		if (src.readByte() == 1) {
			socialAccounts = ParcelUtils.readTypeArrayList(src, FacebookAccount.CREATOR);
		}
		email = src.readString();
		fullName = src.readString();
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(username);
		if (socialAccounts != null && !socialAccounts.isEmpty()) {
			dest.writeByte((byte) 1);
			ParcelUtils.writeTypeList(dest, socialAccounts);
		} else {
			dest.writeByte((byte) 0);
		}
		dest.writeString(email);
		dest.writeString(fullName);
		
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addSocialAccount(FacebookAccount account) {
		List<FacebookAccount> accounts = getSocialAccounts();
		accounts.add(account);
	}
	
	public List<FacebookAccount> getSocialAccounts() {
		if (socialAccounts == null)
			socialAccounts = new LinkedList<FacebookAccount>();
		
		return socialAccounts;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public Date getDateOfBirth() {
//		return dateOfBirth;
//	}
//
//	public void setDateOfBirth(Date dateOfBirth) {
//		this.dateOfBirth = dateOfBirth;
//	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	/**
	 * It returns display name for this user.
	 * Currently, full name if available, username otherwise.
	 * @return
	 */
	public String getDisplayName() {
		return fullName == null ? username : fullName;
	}
	
	public String getFacebookAccountId() {
		if (socialAccounts == null) {
			return null;
		}
		
		for (FacebookAccount account : socialAccounts) {
			return String.valueOf(account.getId());
		}
		
		return null;
	}
	
	public void setFacebookAccountId(String facebookAccountId) {
		if (facebookAccountId == null || "".equals(facebookAccountId)) {
			removeFacebookAccountId();
			return;
		}
		if (socialAccounts == null) {
			socialAccounts = new ArrayList<FacebookAccount>();
		}

		FacebookAccount account = null;
		for (FacebookAccount iAccount : socialAccounts) {
			account = iAccount;
		}
		
		if (account == null) {
			account = new FacebookAccount();
			socialAccounts.add(account);
		}
		account.setId(facebookAccountId);
	}
	
	private void removeFacebookAccountId() {
		if (socialAccounts == null) {
			return;
		}

		int ixToRemove = -1;
		for (int i = 0; i < socialAccounts.size(); i++) {
				ixToRemove = i;
				break;
		}
		
		if (ixToRemove == -1) {
			return;
		}
		
		socialAccounts.remove(ixToRemove);
	}
	
//	public static class UserInGroupComparator implements Comparator<User> {
//		@Override
//		public int compare(User lhs, User rhs) {
//			if (lhs == rhs) {
//				return 0;
//			}
//			if (lhs == null) {
//				return 1;
//			}
//			if (rhs == null) {
//				return -1;
//			}
//			if (lhs.getId() < rhs.getId()) {
//				return -1;
//			} else if (lhs.getId() > rhs.getId()) {
//				return 1;
//			} else {
//				return 0;
//			}
//		}
//		
//	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o != null && o instanceof User) {
			User oUser = (User)o;
			return oUser.getId() == this.getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public String toString() {
		return getId() + "-" + getFullName();
	}
	
}