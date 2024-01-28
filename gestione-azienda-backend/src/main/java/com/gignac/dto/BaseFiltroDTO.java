package com.gignac.dto;

public abstract class BaseFiltroDTO {
	
	private String ordinamento;
	private String direzione;
	private Integer limite;
	private Integer offset;
	
	public String getOrdinamento() {
		return ordinamento;
	}
	
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	
	public String getDirezione() {
		return direzione;
	}
	
	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}
	
	public Integer getLimite() {
		return limite;
	}
	
	public void setLimite(Integer limite) {
		this.limite = limite;
	}
	
	public Integer getOffset() {
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
}
