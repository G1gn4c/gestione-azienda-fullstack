package com.gignac.repository.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gignac.AbstractTest;
import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.entity.Azienda;
import com.gignac.entity.Impiegato;
import com.gignac.enumeration.Sesso;
import com.gignac.repository.jdbc.impl.ImpiegatoJdbcRepositoryImpl;
import com.gignac.repository.jdbc.rowmapper.AziendaRowMapper;

class ImpiegatoJdbcRepositoryTest extends AbstractTest {

	private JdbcTemplate jdbcTemplate;

	private ImpiegatoJdbcRepository impiegatoJdbcRepository;
	
	private static final AziendaRowMapper AZIENDA_ROW_MAPPER = new AziendaRowMapper();

	@BeforeEach
	void setUp() throws Exception {
		jdbcTemplate = ottieniJdbcTemplate();
		impiegatoJdbcRepository = new ImpiegatoJdbcRepositoryImpl(jdbcTemplate);
	}

	@Test
	void testCerca() {
		UUID uuid = UUID.randomUUID();
		Azienda azienda = new Azienda(
				null, 
				FAKER.company().name() + uuid.toString(), 
				FAKER.address().fullAddress(), 
				FAKER.internet().safeEmailAddress(), 
				null
		);
		String sql = """
				INSERT INTO \"gestione-azienda\".azienda (id, nome, indirizzo, email)
				VALUES (nextval('\"gestione-azienda\".azienda_id_seq'), ?, ?, ?)
				""";
		jdbcTemplate.update(
				sql, 
				azienda.getNome(), 
				azienda.getIndirizzo(), 
				azienda.getEmail()
		);
		sql = """
				SELECT *
				FROM \"gestione-azienda\".azienda
				""";
		List<Azienda> aziende = jdbcTemplate.query(sql, AZIENDA_ROW_MAPPER);
		Long idAzienda = aziende.stream()
		       .filter(a -> a.getNome().contains(uuid.toString()))
		       .findFirst()
		       .get()
		       .getId();
		
		String nome = "Gianluca";
		String cognome = "Vitto";
		Sesso sesso = Sesso.MASCHIO;
		LocalDate dataNascita = LocalDate.of(1993, 11, 20);
		String email = "gianluca@email.com";
		Impiegato impiegato = new Impiegato(
				null, 
				nome, 
				cognome, 
				sesso, 
				dataNascita, 
				email, 
				new Azienda(idAzienda, null, null, null, null)
		);
		sql = """
				INSERT INTO \"gestione-azienda\".impiegato (id, nome, cognome, sesso, email, data_nascita, id_azienda)
				VALUES (nextval('\"gestione-azienda\".impiegato_id_seq'), ?, ?, ?, ?, ?, ?)
				""";
		jdbcTemplate.update(
				sql, 
				nome, 
				cognome, 
				sesso.name(), 
				email, 
				dataNascita,
				idAzienda
		);
		
		ImpiegatoFiltroDTO impiegatoFiltroDTO = new ImpiegatoFiltroDTO();
		impiegatoFiltroDTO.setNome("anlu");
		impiegatoFiltroDTO.setCognome("itt");
		impiegatoFiltroDTO.setSesso(sesso);
		impiegatoFiltroDTO.setDataNascitaDa(LocalDate.of(1992, 10, 19));
		impiegatoFiltroDTO.setDataNascitaA(LocalDate.of(1994, 12, 21));
		impiegatoFiltroDTO.setEmail("uca@e");
		impiegatoFiltroDTO.setOrdinamento("nome");
		impiegatoFiltroDTO.setDirezione("ASC");
		impiegatoFiltroDTO.setLimite(2);
		impiegatoFiltroDTO.setOffset(0);
		
		List<Impiegato> impiegati = impiegatoJdbcRepository.cerca(impiegatoFiltroDTO);
		assertThat(impiegati).hasSize(1);
		
		Impiegato impiegatoAttuale = impiegati.get(0);
		assertThat(impiegatoAttuale.getId()).isNotNull();
		assertThat(impiegatoAttuale.getNome()).isEqualTo(impiegato.getNome());
		assertThat(impiegatoAttuale.getCognome()).isEqualTo(impiegato.getCognome());
		assertThat(impiegatoAttuale.getSesso()).isEqualTo(impiegato.getSesso());
		assertThat(impiegatoAttuale.getDataNascita()).isEqualTo(impiegato.getDataNascita());
		assertThat(impiegatoAttuale.getEmail()).isEqualTo(impiegato.getEmail());
		assertThat(impiegatoAttuale.getAzienda().getId()).isEqualTo(impiegato.getAzienda().getId());
	}

}
