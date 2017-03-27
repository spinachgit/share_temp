package com.spinach.business.website;

import java.util.Collection;
import java.util.List;

import com.spinach.business.enums.PageTarget;
import com.spinach.framework.sys.business.BusinessObjectServiceMgr;
import com.spinach.framework.sys.exception.ServiceException;

public interface WebsiteServiceMgr extends BusinessObjectServiceMgr {
	String SERVICE_NAME = "websiteServiceMgr";

	String createPage(Page page) throws ServiceException;

	void updatePage(Page page);

	void deletePage(int id);

	Page getPage(int id);

	List<Page> getPages();

	List<Page> getPages(PageTarget target);

	Collection<Template> getTemplates();

	Template getTemplateByName(String templateName);

	Template getDefaultTemplate();

	Website getWebsite();

	void saveWebsite(Website website) throws ServiceException;

}
