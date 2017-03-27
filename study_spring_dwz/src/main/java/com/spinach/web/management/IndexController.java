package com.spinach.web.management;

//import javax.inject.Inject;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spinach.web.BaseController;

@Controller("management.indexController")
@RequestMapping("/management")
public class IndexController extends BaseController{

	@RequestMapping("")
	public String index(Model model) {

		model.addAttribute("now", new Date());
		
		return "/management/index";
	}

	
}