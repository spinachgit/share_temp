package com.spinach.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spinach.business.enums.PageTarget;
import com.spinach.business.website.Page;
import com.spinach.business.website.WebsiteServiceMgr;

@Controller("webpageController")
@RequestMapping(value = "/webpage")
public class WebpageController extends BaseController {
	@Autowired
	private WebsiteServiceMgr websiteMgr;
	
	@RequestMapping("/{pageId}")
	public String edit(@PathVariable("pageId") int pageId, Model model) {
		Page page = websiteMgr.getPage(pageId);

		model.addAttribute("page", page);
		model.addAttribute("pageTargets", PageTarget.values());
		
		return "/webpage";
	}
	
}
