package com.gignac.repository.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gignac.entity.Azienda;
import com.gignac.entity.Impiegato;
import com.gignac.enumeration.Sesso;

public class ImpiegatoRowMapper implements RowMapper<Impiegato> {

	@Override
	public Impiegato mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Impiegato(
				rs.getLong("id"), 
				rs.getString("nome"), 
				rs.getString("cognome"), 
				Sesso.valueOf(rs.getString("sesso")), 
				rs.getDate("data_nascita").toLocalDate(), 
				rs.getString("email"), 
				new Azienda(rs.getLong("id_azienda"), null, null, null, null)
		);
	}

}
