package com.gignac.dto;

import java.time.LocalDate;

import com.gignac.enumeration.Sesso;

public class ImpiegatoReadDTO {

	private Long id;
	private String nome;
	private String cognome;
	private Sesso sesso;
	private LocalDate dataNascita;
	private String email;
	private AziendaReadDTO azienda;
	
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
	
	public Sesso getSesso() {
		return sesso;
	}
	
	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}
	
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public AziendaReadDTO getAzienda() {
		return azienda;
	}
	
	public void setAzienda(AziendaReadDTO azienda) {
		this.azienda = azienda;
	}
	
}
