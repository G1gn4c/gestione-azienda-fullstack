package com.gignac.repository.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gignac.AbstractTest;
import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.entity.Ruolo;
import com.gignac.entity.Utente;
import com.gignac.repository.jdbc.impl.UtenteJdbcRepositoryImpl;

class UtenteJdbcRepositoryTest extends AbstractTest {
	
	private JdbcTemplate jdbcTemplate;
	
	private UtenteJdbcRepository utenteJdbcRepository;

	@BeforeEach
	void setUp() throws Exception {
		jdbcTemplate = ottieniJdbcTemplate();
		utenteJdbcRepository = new UtenteJdbcRepositoryImpl(jdbcTemplate);
	}

	@Test
	void testCerca() {
		String nome = "Pippo";
		String cognome = "Baudo";
		String username = "PippoTheBest";
		String password = "password";
		Ruolo ruolo = new Ruolo(1L, null, null, null);
		Utente utente = new Utente(
				null, 
				nome, 
				cognome, 
				username, 
				password, 
				ruolo
		);
		String sql = """
				INSERT INTO \"gestione-azienda\".utente (id, nome, cognome, username, password, id_ruolo)
				VALUES (nextval('\"gestione-azienda\".utente_id_seq'), ?, ?, ?, ?, ?)
				""";
		jdbcTemplate.update(
				sql, 
				nome, 
				cognome, 
				username, 
				password, 
				ruolo.getId()
		);
		
		UtenteFiltroDTO utenteFiltroDTO = new UtenteFiltroDTO();
		utenteFiltroDTO.setNome("ipp");
		utenteFiltroDTO.setCognome("aud");
		utenteFiltroDTO.setUsername("oThe");
		utenteFiltroDTO.setOrdinamento("nome");
		utenteFiltroDTO.setDirezione("ASC");
		utenteFiltroDTO.setLimite(2);
		utenteFiltroDTO.setOffset(0);
		
		List<Utente> utenti = utenteJdbcRepository.cerca(utenteFiltroDTO);
		assertThat(utenti).hasSize(1);
		
		Utente utenteAttuale = utenti.get(0);
		assertThat(utenteAttuale.getId()).isNotNull();
		assertThat(utenteAttuale.getNome()).isEqualTo(utente.getNome());
		assertThat(utenteAttuale.getCognome()).isEqualTo(utente.getCognome());
		assertThat(utenteAttuale.getUsername()).isEqualTo(utente.getUsername());
		assertThat(utenteAttuale.getPassword()).isEqualTo(utente.getPassword());
		assertThat(utenteAttuale.getRuolo().getId()).isEqualTo(utente.getRuolo().getId());
	}

}
