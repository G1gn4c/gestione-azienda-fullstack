package com.gignac.repository.jdbc;

import java.util.List;

import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.entity.Utente;

public interface UtenteJdbcRepository {
	
	public List<Utente> cerca(UtenteFiltroDTO utenteFiltroDTO);
	
}
