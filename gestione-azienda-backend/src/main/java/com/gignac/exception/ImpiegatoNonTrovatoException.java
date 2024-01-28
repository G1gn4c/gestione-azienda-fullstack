package com.gignac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ImpiegatoNonTrovatoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5081905461570706243L;

	public ImpiegatoNonTrovatoException(String messaggio) {
		super(messaggio);
	}

}
