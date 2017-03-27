package com.spinach.framework.context;

import com.spinach.business.website.Website;
import com.spinach.framework.user.User;

public interface AppContext {

	User getUser();

	void setUser(User user);

	Website getWebsite();

	void setWebsite(Website website);

}
