package com.openshift.jobfinder;

import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openshift.jobfinder.account.AccountRepository;

@Controller
public class HomeController {

	private final Provider<ConnectionRepository> connectionRepositoryProvider;

	private final AccountRepository accountRepository;

	@Inject
	public HomeController(
			Provider<ConnectionRepository> connectionRepositoryProvider,
			AccountRepository accountRepository) {
		this.connectionRepositoryProvider = connectionRepositoryProvider;
		this.accountRepository = accountRepository;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Principal currentUser, Model model) {
		model.addAttribute("connectionsToProviders", getConnectionRepository()
				.findAllConnections());
		model.addAttribute(accountRepository.findAccountByUsername(currentUser
				.getName()));
		return "home";
	}

	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
}
