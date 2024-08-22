package com.enterprise.accounts.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.accounts.dto.CustomerDetailsDto;
import com.enterprise.accounts.dto.ErrorResponseDto;
import com.enterprise.accounts.service.ICustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;

@Tag(
		name = "CRUD REST APIs for Customer in Entailment,",
		description = "CRUD REST APIs in Entailment to CREATE, UPDATE, FETCH and DELETE customer details")
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	private final ICustomerService iCustomerService;
	
	public CustomerController(ICustomerService iCustomerService) {
		this.iCustomerService = iCustomerService;
	}
	
	@Operation(
			summary = "Fetch Customer Details REST API",
			description = "REST API to fetch Customer Details in Entailment")
	@ApiResponses({
			@ApiResponse(
				responseCode = "201",
				description = "HTTP Status OK"
			),
			@ApiResponse(
				responseCode = "500",
				description = "HTTP Status Internal Server Error",
				content = @Content(
						schema = @Schema(implementation = ErrorResponseDto.class)
						)
			)
		}
	)
	@GetMapping("/fetchCustomerDetails")
	public ResponseEntity<CustomerDetailsDto>fetchCustomerDetails(@RequestHeader("entailment-correlation-id") String correlationId, 
			@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
		logger.debug("fetchCustomerDetails method start");
		CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber, correlationId);
		logger.debug("fetchCustomerDetails method end");
		return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
	}

}
