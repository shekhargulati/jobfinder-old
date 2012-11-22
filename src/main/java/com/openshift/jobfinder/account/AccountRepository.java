package com.openshift.jobfinder.account;

import com.openshift.jobfinder.domain.Account;

public interface AccountRepository {
	
	void createAccount(Account account) throws UsernameAlreadyInUseException;

	Account findAccountByUsername(String username);
	
}
