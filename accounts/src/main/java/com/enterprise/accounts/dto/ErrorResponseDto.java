package com.enterprise.accounts.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
@Schema(
		name="Error Response",
		description = "Schema to hold error successfull response information"
)
public class ErrorResponseDto {
	
	@Schema(
			description="Api invoked by client"
	)
	private String apiPath;
	
	@Schema(
			description="Error code represent the error happened"
	)
	private HttpStatus errorCode;
	
	@Schema(
			description="Error message represent the error happened"
	)
	private String errorMessage;
	
	@Schema(
			description="Time representint when the error happened", example = "200"
	)
	private LocalDateTime errorTime;
}
