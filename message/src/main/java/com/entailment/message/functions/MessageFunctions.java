package com.entailment.message.functions;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.entailment.message.dto.AccountMsgDto;

@Configuration
public class MessageFunctions {
	
	private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);
	
	@Bean
	public Function<AccountMsgDto, AccountMsgDto> email() {
		return accountsMsgDto -> {
			log.info("Sending email with the details:" + accountsMsgDto.toString());
			return accountsMsgDto;
		};
	}
	
	@Bean
	public Function<AccountMsgDto, Long> sms() {
		return accountsMsgDto -> {
			log.info("Sending sms with the details:" + accountsMsgDto.toString());
			return accountsMsgDto.accountNumber();
		};
	}

}
