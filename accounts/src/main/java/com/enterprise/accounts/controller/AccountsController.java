package com.enterprise.accounts.controller;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.accounts.constants.AccountsConstants;
import com.enterprise.accounts.dto.AccountsContactInfoDto;
import com.enterprise.accounts.dto.CustomerDto;
import com.enterprise.accounts.dto.ErrorResponseDto;
import com.enterprise.accounts.dto.ResponseDto;
import com.enterprise.accounts.service.IAccountsService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Tag(
		name = "CRUD REST APIs in Entailment,",
		description = "CRUD REST APIs in Entailment to CREATE, UPDATE, FETCH and DELETE account details")
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	private final IAccountsService iAccountService;
	
	public AccountsController(IAccountsService iAccountService) {
		this.iAccountService = iAccountService;
		
	}
	
	@Value("${build.version}")
	private String buildVersion;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private AccountsContactInfoDto accountsInfoDto;
	
	@Operation(
			summary = "Create REST API",
			description = "REST API to create new Customer & Account in Entailment")
	@ApiResponse(
			responseCode = "201",
			description = "HTTP Status CREATED")
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
		
		iAccountService.createAccount(customerDto);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
	}
	
	@Operation(
			summary = "FETCH REST API",
			description = "REST API to fetch new Customer & Account in Entailment")
	@ApiResponse(
			responseCode = "201",
			description = "HTTP Status OK")
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam 
			@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
														String mobileNumber) {
		CustomerDto customerDto = iAccountService.fetchAccount(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}
	
	@Operation(
			summary = "Update REST API",
			description = "REST API to Update new Customer & Account in Entailment")
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
			
	@PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    	}
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                        @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccountService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }
    
    @Operation(
			summary = "Get Build information",
			description = "Get Build information that is deployed into accounts microservices")
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
    @Retry(name="getBuildInfo",fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuilInfo() throws TimeoutException {
    	logger.debug("getBuildInfo() method Invoked");
    	//throw new TimeoutException();
    	return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }
    
    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
    	logger.debug("getBuildInfoFallback() method Invoked");
    	return ResponseEntity
                .status(HttpStatus.OK)
                .body("0.9");
    }
    
    @Operation(
			summary = "Get Java Version",
			description = "Get Java Version that is deployed into accounts microservices")
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
    @RateLimiter(name="getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
    	return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }
    
    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
    	return ResponseEntity
                .status(HttpStatus.OK)
                .body("Java 17"); 
    }
    
    @Operation(
			summary = "Get Contact Info",
			description = "Get Contact Info that is deployed into accounts microservices")
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
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
    	return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsInfoDto);
    }
	
}
