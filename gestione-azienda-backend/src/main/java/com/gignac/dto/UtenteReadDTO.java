package com.gignac.dto;

public class UtenteReadDTO {

	private Long id;
	private String nome;
	private String cognome;
	private String username;
	private RuoloReadDTO ruolo;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
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
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public RuoloReadDTO getRuolo() {
		return ruolo;
	}
	
	public void setRuolo(RuoloReadDTO ruolo) {
		this.ruolo = ruolo;
	}
	
}
