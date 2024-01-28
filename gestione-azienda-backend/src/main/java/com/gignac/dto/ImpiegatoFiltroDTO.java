package com.gignac.dto;

import java.time.LocalDate;

import com.gignac.enumeration.Sesso;

public class ImpiegatoFiltroDTO extends BaseFiltroDTO {
	
	private String nome;
	private String cognome;
	private Sesso sesso;
	private LocalDate dataNascitaDa;
	private LocalDate dataNascitaA;
	private String email;
	
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
	
	public LocalDate getDataNascitaDa() {
		return dataNascitaDa;
	}
	
	public void setDataNascitaDa(LocalDate dataNascitaDa) {
		this.dataNascitaDa = dataNascitaDa;
	}
	
	public LocalDate getDataNascitaA() {
		return dataNascitaA;
	}
	
	public void setDataNascitaA(LocalDate dataNascitaA) {
		this.dataNascitaA = dataNascitaA;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}
