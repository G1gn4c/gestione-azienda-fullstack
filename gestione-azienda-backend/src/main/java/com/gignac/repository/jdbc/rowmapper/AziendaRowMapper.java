package com.gignac.repository.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gignac.entity.Azienda;

public class AziendaRowMapper implements RowMapper<Azienda> {

	@Override
	public Azienda mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Azienda(
				rs.getLong("id"), 
				rs.getString("nome"), 
				rs.getString("indirizzo"), 
				rs.getString("email"), 
				null
		);
	}

}
