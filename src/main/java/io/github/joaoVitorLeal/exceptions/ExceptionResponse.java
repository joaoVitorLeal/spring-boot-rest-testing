package io.github.joaoVitorLeal.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExceptionResponse implements Serializable {
	// realizar cache da instancia de uma classe
	private static final long serialVersionUID = 1L;
	
	private LocalDateTime timeStamp;
	private String message;
	private String details;
	
	public ExceptionResponse(LocalDateTime timeStamp, String message, String details) {
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
