package com.gignac.dto;

import java.time.LocalDate;

public class ImpiegatoCreateDTO extends ImpiegatoDTO {

	private LocalDate dataNascita;

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

}
