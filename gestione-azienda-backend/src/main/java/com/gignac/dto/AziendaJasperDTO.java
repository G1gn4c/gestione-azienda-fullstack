package com.gignac.dto;

import java.util.List;

public class AziendaJasperDTO {

	private String nome;
	private String indirizzo;
	private String email;
	private List<ImpiegatoJasperDTO> impiegati;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public List<ImpiegatoJasperDTO> getImpiegati() {
		return impiegati;
	}

	public void setImpiegati(List<ImpiegatoJasperDTO> impiegati) {
		this.impiegati = impiegati;
	}
	
}
