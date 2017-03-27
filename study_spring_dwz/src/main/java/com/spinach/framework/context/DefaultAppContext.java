package com.spinach.framework.context;

import com.spinach.business.website.Website;
import com.spinach.framework.user.User;

public class DefaultAppContext implements AppContext {

	private User user = null;

	private Website website = null;

	public DefaultAppContext() {
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

}
