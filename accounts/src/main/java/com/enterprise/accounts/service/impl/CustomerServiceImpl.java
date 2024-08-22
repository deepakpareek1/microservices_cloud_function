package com.enterprise.accounts.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enterprise.accounts.dto.AccountsDto;
import com.enterprise.accounts.dto.CardsDto;
import com.enterprise.accounts.dto.CustomerDetailsDto;
import com.enterprise.accounts.dto.CustomerDto;
import com.enterprise.accounts.dto.LoansDto;
import com.enterprise.accounts.entity.Accounts;
import com.enterprise.accounts.entity.Customer;
import com.enterprise.accounts.exception.ResourceNotFoundException;
import com.enterprise.accounts.mapper.AccountsMapper;
import com.enterprise.accounts.mapper.CustomerMapper;
import com.enterprise.accounts.repository.AccountsRepository;
import com.enterprise.accounts.repository.CustomerRepository;
import com.enterprise.accounts.service.ICustomerService;
import com.enterprise.accounts.service.client.CardsFeignClient;
import com.enterprise.accounts.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

	private AccountsRepository accountRepository;
	private CustomerRepository customerRepository;
	private CardsFeignClient cardsFeignClient;
	private LoansFeignClient loanFeignClient;
	
	/**
	 * 
	 * @param mobileNumber - Input mobile number
	 * @return Customer Details based on given mobile number
	 */	
	@Override
	public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
		 Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
	                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        
        ResponseEntity<LoansDto> loansDtoReponseEntity = loanFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if (null != loansDtoReponseEntity) {
        	customerDetailsDto.setLoansDto(loansDtoReponseEntity.getBody());
        }
        
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if (null != cardsDtoResponseEntity) {
        	customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }
        
        return customerDetailsDto;
	}

}
