package com.openshift.jobfinder.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LinkedInController {

	@Inject
	private ConnectionRepository connectionRepository;

	@RequestMapping(value = "/linkedin", method = RequestMethod.GET)
	public String home(Principal currentUser, Model model) {
		Connection<LinkedIn> connection = connectionRepository
				.findPrimaryConnection(LinkedIn.class);
		if (connection == null) {
			return "redirect:/connect/linkedin";
		}
		LinkedInProfile profile = connection.getApi().profileOperations()
				.getUserProfile();
		model.addAttribute("profile", profile);
		return "linkedin/profile";
	}

	
}