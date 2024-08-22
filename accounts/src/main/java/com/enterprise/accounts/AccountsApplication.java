package com.enterprise.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.enterprise.accounts.dto.AccountsContactInfoDto;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value= {AccountsContactInfoDto.class})
@OpenAPIDefinition(
		info = @Info(
				title = "Account microservices REST API Documentation",
				description = "Entailment Accounts micorservices REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Deepak Pareek",
						email = "deepakpareek1@gmail.com",
						url = "www.entailmentinc.com"
					),
					license = @License(
							name = "Apache 2.0",
							url = "www.entailmentinc.com"
					)
				),
				externalDocs = @ExternalDocumentation(
						description = "Entailment Accounts micorservices REST API Documentation",
						url = "www.entailmentinc.com"
				)
		)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
