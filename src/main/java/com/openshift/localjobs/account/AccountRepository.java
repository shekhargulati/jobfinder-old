package com.openshift.localjobs.account;

public interface AccountRepository {
	
	void createAccount(Account account) throws UsernameAlreadyInUseException;

	Account findAccountByUsername(String username);
	
}
