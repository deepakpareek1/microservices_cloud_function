package com.enterprise.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.enterprise.accounts.dto.LoansDto;

@FeignClient(name="loans",fallback=LoansFallBack.class)
public interface LoansFeignClient {

	@GetMapping(value="/api/fetch", consumes = "application/json")
	public ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("entailment-correlation-id") 
					String correlationId, @RequestParam String mobileNumber);
}
