/***********************************************************************
 * Module:  Validator.java
 * Author:  Zhang Huihua
 * Purpose: Defines the Interface Validator
 ***********************************************************************/

package com.spinach.framework.identity;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.spinach.framework.config.Constants;
import com.spinach.framework.context.AppContext;
import com.spinach.framework.context.AppContextHolder;
import com.spinach.framework.context.DefaultAppContext;
import com.spinach.framework.identity.impl.SessionIdentity;
import com.spinach.framework.user.User;

public class Validator implements IdentityProvider {

	private static final Log log = LogFactory.getLog(Validator.class);

	private static ThreadLocal<Validator> validatorHolder = new ThreadLocal<Validator>() {

		protected Validator initialValue() {
			return new Validator();
		}

	};


	private HttpSession session = null;

	private User user = null;

	private Validator() {
	}

	public static Validator getInstance() {
		return validatorHolder.get();
	}

	public void init(HttpSession session) {
		this.session = session;
	}

	public boolean validate() {
		log.debug("will validate session.");
		if (session == null) {
			log.warn("the session is null.");
			return false;
		}

		boolean expired = false;

		try {
			this.user = (User) session
					.getAttribute(Constants.AUTHENTICATION_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.user == null) {
			expired = true;
		}
		if (!expired) {
			if (log.isDebugEnabled()) {
				log.debug("validating session successfully.");
			}
			
			log.debug("validate account successfully.");

			return true;
		}

		log.debug("validating session failed.");

		return false;
	}

	public void confirm() {
		if (this.user == null) {
			throw new IllegalArgumentException("authentication is null.");
		}

		AppContext context = AppContextHolder.getContext();
		if (context == null) {
			context = new DefaultAppContext();
			AppContextHolder.setContext(context);
		}
		
		context.setUser(user);
		
	}

	public void cancel() {
		this.session = null;
		this.user = null;
		AppContext context = AppContextHolder.getContext();
		if (context != null) {
			context.setUser(null);
		}
	}

	public void clear() {
		this.session = null;
		this.user = null;
		AppContext context = AppContextHolder.getContext();
		if (context != null) {
			context.setUser(null);
		}
	}

	public Identity createIdentity(String identityString) {
		if (identityString == null) {
			return null;
		}

		return new SessionIdentity(identityString);
	}
}