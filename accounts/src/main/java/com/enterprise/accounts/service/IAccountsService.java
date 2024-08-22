package com.enterprise.accounts.service;

import com.enterprise.accounts.dto.CustomerDto;

public interface IAccountsService {
	
	/**
	 * 
	 * @param customerDto
	 */
	void createAccount(CustomerDto customerDto);
	
	/**
	 * 
	 * @param mobileNumber
	 * @return Account detail based on a given mobile number
	 */
	CustomerDto fetchAccount(String mobileNumber);
	
	/**
	 * 
	 * @param customerDto
	 * @return boolean indicating if the update account detail is successful or not
	 */
	boolean updateAccount(CustomerDto customerDto);
	
	/**
	 * 
	 * @param mobileNumber
	 * @return boolean indicating if the delete account detail is successful or not
	 */
	boolean deleteAccount(String mobileNumber);

}
