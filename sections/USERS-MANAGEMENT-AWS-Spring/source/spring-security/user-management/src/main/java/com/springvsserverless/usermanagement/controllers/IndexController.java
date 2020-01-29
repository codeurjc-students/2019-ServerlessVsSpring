package com.springvsserverless.usermanagement.controllers;

import com.springvsserverless.usermanagement.user.SessionUserComponent;
import com.springvsserverless.usermanagement.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionUserComponent sessionUserComponent;

	/* Introduce Mock data */
	@PostConstruct
	public void init() {
		/*
		 * for (int i = 0; i < 5; i++) {
		 * 
		 * }
		 */
	}

	@RequestMapping("/")
	public String allData(Model model, Pageable page) {

		if (sessionUserComponent.isLoggedUser()) {
			model.addAttribute("labelLogIn", "My Profile");
			model.addAttribute("labelSignUp", "Log Out");
			model.addAttribute("urlLabelSignUp", "/logOut");
			model.addAttribute("urlLabelLogIn", "/profile/" + sessionUserComponent.getLoggedUser().getInternalName());
			if (sessionUserComponent.getLoggedUser().isAdmin()) {
				model.addAttribute("linkGetStarted", "/admin");
				model.addAttribute("textGetStarted", "Go to admin page");
			} else {
				model.addAttribute("linkGetStarted",
						"/profile/" + sessionUserComponent.getLoggedUser().getInternalName());
				model.addAttribute("textGetStarted", "Go to my profile");
			}

		} else {
			model.addAttribute("labelLogIn", "Log In");
			model.addAttribute("labelSignUp", "Sign Up");
			model.addAttribute("urlLabelSignUp", "/signup");
			model.addAttribute("urlLabelLogIn", "/login");
			model.addAttribute("linkGetStarted", "/login");
			model.addAttribute("textGetStarted", "Get Started Now");
		}

		return "home";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		return "login";
	}

	// @RequestMapping("/searchByName")
	/*
	 * public String courseByName(Model model) {
	 * 
	 * }
	 */

}
