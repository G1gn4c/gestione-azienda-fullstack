package com.gignac.dto;

import com.gignac.enumeration.Sesso;

public class ImpiegatoDTO {

	private String nome;
	private String cognome;
	private Sesso sesso;
	private String email;
	private Long idAzienda;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public Sesso getSesso() {
		return sesso;
	}
	
	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getIdAzienda() {
		return idAzienda;
	}
	
	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}
	
}
