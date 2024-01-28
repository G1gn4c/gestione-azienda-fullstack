package com.gignac.exception;

public class UsernameDuplicatoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7590308205461645566L;
	
	public UsernameDuplicatoException(String messaggio) {
		super(messaggio);
	}

}
