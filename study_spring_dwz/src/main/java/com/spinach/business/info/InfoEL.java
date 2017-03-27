package com.spinach.business.info;

import java.util.ArrayList;
import java.util.List;

import com.spinach.business.enums.PageTarget;
import com.spinach.business.website.Page;
import com.spinach.business.website.Website;
import com.spinach.business.website.WebsiteServiceMgr;
import com.spinach.common.util.EnumUtils;
import com.spinach.framework.context.AppContext;
import com.spinach.framework.context.AppContextHolder;
import com.spinach.framework.context.DefaultAppContext;
import com.spinach.framework.sys.business.BusinessFactory;
import com.spinach.framework.user.User;

public class InfoEL {

	public static List<News> listNews(String type, Integer startIndex,
			Integer count) {

		if (EnumUtils.isDefined(NewsType.values(), type)) {
			NewsServiceMgr manager = BusinessFactory.getInstance().getService(
					NewsServiceMgr.SERVICE_NAME);
			return manager.listNewsOnWeb(NewsType.valueOf(type), startIndex,
					count);
		}
		return new ArrayList<News>();
	}

	public static List<Page> listPages(String target) {
		if (EnumUtils.isDefined(PageTarget.values(), target)) {
			WebsiteServiceMgr manager = BusinessFactory.getInstance()
					.getService(WebsiteServiceMgr.SERVICE_NAME);

			return manager.getPages(PageTarget.valueOf(target));
		}
		return new ArrayList<Page>();
	}

	public static User getContextUser() {
		AppContext appContext = AppContextHolder.getContext();

		if (appContext != null) {
			return appContext.getUser();
		}

		return null;
	}

	public static Website getContextWebsite() {

		AppContext context = AppContextHolder.getContext();

		if (context != null) {
			return context.getWebsite();
		}

		return null;
	}
}
