package com.gignac.dto;

import java.time.LocalDate;

public class ImpiegatoUpdateDTO extends ImpiegatoDTO {
	
	private Long id;
	private LocalDate dataNascita;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	
}
