package com.gignac.dto;

public class AuthenticationResponseDTO {

	private UtenteReadDTO utenteReadDTO;
	private String token;

	public UtenteReadDTO getUtenteReadDTO() {
		return utenteReadDTO;
	}

	public void setUtenteReadDTO(UtenteReadDTO utenteReadDTO) {
		this.utenteReadDTO = utenteReadDTO;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
