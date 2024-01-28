package com.gignac.repository.jdbc.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.gignac.entity.Ruolo;
import com.gignac.entity.Utente;
import com.github.javafaker.Faker;

class UtenteRowMapperTest {
	
	private static final Faker FAKER = new Faker();

	@Test
	void testMapRow() throws SQLException {
		Long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		Utente utente = new Utente(
				id, 
				nome, 
				cognome, 
				username, 
				password, 
				new Ruolo(idRuolo, null, null, null)
		);
		
		UtenteRowMapper utenteRowMapper = new UtenteRowMapper();
		
		ResultSet rs = mock(ResultSet.class);
		when(rs.getLong("id")).thenReturn(id);
		when(rs.getString("nome")).thenReturn(nome);
		when(rs.getString("cognome")).thenReturn(cognome);
		when(rs.getString("username")).thenReturn(username);
		when(rs.getString("password")).thenReturn(password);
		when(rs.getLong("id_ruolo")).thenReturn(idRuolo);
		
		Utente utenteAttuale = utenteRowMapper.mapRow(rs, 0);
		
		assertThat(utenteAttuale.getId()).isEqualTo(utente.getId());
		assertThat(utenteAttuale.getNome()).isEqualTo(utente.getNome());
		assertThat(utenteAttuale.getCognome()).isEqualTo(utente.getCognome());
		assertThat(utenteAttuale.getUsername()).isEqualTo(utente.getUsername());
		assertThat(utenteAttuale.getPassword()).isEqualTo(utente.getPassword());
		assertThat(utenteAttuale.getRuolo().getId()).isEqualTo(utente.getRuolo().getId());
		assertThat(utenteAttuale.getRuolo().getCodice()).isNull();
		assertThat(utenteAttuale.getRuolo().getDescrizione()).isNull();
		assertThat(utenteAttuale.getRuolo().getUtenti()).isNull();
	}

}
