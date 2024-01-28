package com.gignac.repository.jdbc.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import com.gignac.entity.Azienda;
import com.gignac.entity.Impiegato;
import com.gignac.enumeration.Sesso;
import com.github.javafaker.Faker;

class ImpiegatoRowMapperTest {
	
	private static final Faker FAKER = new Faker();

	@Test
	void testMapRow() throws SQLException {
		long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		Sesso sesso = Sesso.MASCHIO;
		String email = FAKER.internet().safeEmailAddress();
		long idAzienda = 1L;
		LocalDate dataNascita = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Impiegato impiegato = new Impiegato(
				id, 
				nome, 
				cognome, 
				sesso, 
				dataNascita, 
				email, 
				new Azienda(idAzienda, null, null, null, null)
		);
		
		ImpiegatoRowMapper impiegatoRowMapper = new ImpiegatoRowMapper();
		
		ResultSet rs = mock(ResultSet.class);
		when(rs.getLong("id")).thenReturn(id);
		when(rs.getString("nome")).thenReturn(nome);
		when(rs.getString("cognome")).thenReturn(cognome);
		when(rs.getString("sesso")).thenReturn(sesso.name());
		when(rs.getDate("data_nascita")).thenReturn(Date.valueOf(dataNascita));
		when(rs.getString("email")).thenReturn(email);
		when(rs.getLong("id_azienda")).thenReturn(idAzienda);
		
		Impiegato impiegatoAttuale = impiegatoRowMapper.mapRow(rs, 0);
		
		assertThat(impiegatoAttuale.getId()).isEqualTo(impiegato.getId());
		assertThat(impiegatoAttuale.getNome()).isEqualTo(impiegato.getNome());
		assertThat(impiegatoAttuale.getCognome()).isEqualTo(impiegato.getCognome());
		assertThat(impiegatoAttuale.getSesso()).isEqualTo(impiegato.getSesso());
		assertThat(impiegatoAttuale.getDataNascita()).isEqualTo(impiegato.getDataNascita());
		assertThat(impiegatoAttuale.getEmail()).isEqualTo(impiegato.getEmail());
		assertThat(impiegatoAttuale.getAzienda().getId()).isEqualTo(impiegato.getAzienda().getId());
		assertThat(impiegatoAttuale.getAzienda().getNome()).isNull();
		assertThat(impiegatoAttuale.getAzienda().getIndirizzo()).isNull();
		assertThat(impiegatoAttuale.getAzienda().getEmail()).isNull();
		assertThat(impiegatoAttuale.getAzienda().getImpiegati()).isNull();
	}

}
