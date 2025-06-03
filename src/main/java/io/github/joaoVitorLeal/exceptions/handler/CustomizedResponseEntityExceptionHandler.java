package io.github.joaoVitorLeal.exceptions.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.joaoVitorLeal.exceptions.ExceptionResponse;
import io.github.joaoVitorLeal.exceptions.UnsupportedMathOperationException;
import lombok.extern.slf4j.Slf4j;

@RestController
@ControllerAdvice
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
		log.info("Handling exception> {} ", ex);
		log.error("Exception occured: ", ex);

		ExceptionResponse exceptionResponse = new ExceptionResponse(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(true)
			);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UnsupportedMathOperationException.class)
	public final ResponseEntity<ExceptionResponse> handleUnsupportedMathOperationException(
	    UnsupportedMathOperationException ex, WebRequest request) {
	    
	    ExceptionResponse exceptionResponse = new ExceptionResponse(
	        LocalDateTime.now(), 
	        ex.getMessage(), 
	        request.getDescription(true)
	    );
	    
	    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}
