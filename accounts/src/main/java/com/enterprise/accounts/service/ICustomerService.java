package com.enterprise.accounts.service;

import com.enterprise.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {
	
	/**
	 * 
	 * @param mobileNumber - Input mobile number
	 * @return Customer Details based on given mobile number
	 */
	CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);

}
