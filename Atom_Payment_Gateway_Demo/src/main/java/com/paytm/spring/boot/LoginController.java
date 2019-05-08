package com.paytm.spring.boot;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.paytm.bean.LoginBean;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@Controller
public class LoginController {
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String init(Model model) {
		model.addAttribute("msg", "Please Enter Your Login Details");
		return "login.jsp";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(Model model,
			@ModelAttribute("loginBean") LoginBean loginBean) {
		if (loginBean != null && loginBean.getUserName() != null
				& loginBean.getPassword() != null) {
			if (loginBean.getUserName().equals("vaibhav")
					&& loginBean.getPassword().equals("vaibhav123")) {
				model.addAttribute("msg", loginBean.getUserName());
				return "success.jsp";
			} else {
				model.addAttribute("error", "Invalid Details");
				return "login.jsp";
			}
		} else {
			model.addAttribute("error", "Please enter Details");
			return "login.jsp";
		}
	}
}
