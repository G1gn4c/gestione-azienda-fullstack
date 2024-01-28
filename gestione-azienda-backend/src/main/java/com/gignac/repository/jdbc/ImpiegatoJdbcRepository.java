package com.gignac.repository.jdbc;

import java.util.List;

import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.entity.Impiegato;

public interface ImpiegatoJdbcRepository {
	
	public List<Impiegato> cerca(ImpiegatoFiltroDTO impiegatoFiltroDTO);
	
}
