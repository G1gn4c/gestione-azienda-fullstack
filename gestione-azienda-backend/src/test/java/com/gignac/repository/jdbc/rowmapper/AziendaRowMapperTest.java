package com.gignac.repository.jdbc.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.gignac.entity.Azienda;
import com.github.javafaker.Faker;

class AziendaRowMapperTest {
	
	private static final Faker FAKER = new Faker();

	@Test
	void testMapRow() throws SQLException {
		long id = 1L;
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		Azienda azienda = new Azienda(
				id, 
				nome, 
				indirizzo, 
				email, 
				null
		);
		
		AziendaRowMapper aziendaRowMapper = new AziendaRowMapper();
		
		ResultSet rs = mock(ResultSet.class);
		when(rs.getLong("id")).thenReturn(id);
		when(rs.getString("nome")).thenReturn(nome);
		when(rs.getString("indirizzo")).thenReturn(indirizzo);
		when(rs.getString("email")).thenReturn(email);
		
		Azienda aziendaAttuale = aziendaRowMapper.mapRow(rs, 0);
		
		assertThat(aziendaAttuale.getId()).isEqualTo(azienda.getId());
		assertThat(aziendaAttuale.getNome()).isEqualTo(azienda.getNome());
		assertThat(aziendaAttuale.getIndirizzo()).isEqualTo(azienda.getIndirizzo());
		assertThat(aziendaAttuale.getEmail()).isEqualTo(azienda.getEmail());
		assertThat(aziendaAttuale.getImpiegati()).isNull();
	}

}
