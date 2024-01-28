package com.gignac.repository.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gignac.entity.Ruolo;
import com.gignac.entity.Utente;

public class UtenteRowMapper implements RowMapper<Utente> {

	@Override
	public Utente mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Utente(
				rs.getLong("id"), 
				rs.getString("nome"), 
				rs.getString("cognome"), 
				rs.getString("username"), 
				rs.getString("password"), 
				new Ruolo(rs.getLong("id_ruolo"), null, null, null)
		);
	}

}
