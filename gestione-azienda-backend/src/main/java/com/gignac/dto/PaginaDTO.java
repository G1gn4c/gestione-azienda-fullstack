package com.gignac.dto;

import java.util.List;

public class PaginaDTO<T> {

	private List<T> payload;
	private Long totale;
	
	public List<T> getPayload() {
		return payload;
	}
	
	public void setPayload(List<T> payload) {
		this.payload = payload;
	}
	
	public Long getTotale() {
		return totale;
	}
	
	public void setTotale(Long totale) {
		this.totale = totale;
	}
	
}
