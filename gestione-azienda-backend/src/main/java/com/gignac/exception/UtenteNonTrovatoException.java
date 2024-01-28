package com.gignac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UtenteNonTrovatoException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5601786611938524129L;

	public UtenteNonTrovatoException(String messaggio) {
		super(messaggio);
	}

}
