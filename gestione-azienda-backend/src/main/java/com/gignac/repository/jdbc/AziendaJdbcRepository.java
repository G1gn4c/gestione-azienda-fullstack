package com.gignac.repository.jdbc;

import java.util.List;

import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.entity.Azienda;

public interface AziendaJdbcRepository {
	
	public List<Azienda> cerca(AziendaFiltroDTO aziendaFiltroDTO);
	
}
