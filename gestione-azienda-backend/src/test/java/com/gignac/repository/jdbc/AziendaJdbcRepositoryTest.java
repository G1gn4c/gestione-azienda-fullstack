package com.gignac.repository.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gignac.AbstractTest;
import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.entity.Azienda;
import com.gignac.repository.jdbc.impl.AziendaJdbcRepositoryImpl;

class AziendaJdbcRepositoryTest extends AbstractTest {
	
	private JdbcTemplate jdbcTemplate;

	private AziendaJdbcRepository aziendaJdbcRepository;

	@BeforeEach
	void setUp() throws Exception {
		jdbcTemplate = ottieniJdbcTemplate();
		aziendaJdbcRepository = new AziendaJdbcRepositoryImpl(jdbcTemplate);
	}

	@Test
	void testCerca() {
		String nome = "NomeAzienda";
		String indirizzo = "IndirizzoAzienda";
		String email = "emailazienda@email.com";
		Azienda azienda = new Azienda(
				null, 
				nome, 
				indirizzo, 
				email, 
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
		
		AziendaFiltroDTO aziendaFiltroDTO = new AziendaFiltroDTO();
		aziendaFiltroDTO.setNome("meAzie");
		aziendaFiltroDTO.setIndirizzo("izzoAzi");
		aziendaFiltroDTO.setEmail("enda@em");
		aziendaFiltroDTO.setOrdinamento("nome");
		aziendaFiltroDTO.setDirezione("ASC");
		aziendaFiltroDTO.setLimite(2);
		aziendaFiltroDTO.setOffset(0);
		
		List<Azienda> aziende = aziendaJdbcRepository.cerca(aziendaFiltroDTO);
		assertThat(aziende).hasSize(1);
		
		Azienda aziendaAttuale = aziende.get(0);
		assertThat(aziendaAttuale.getId()).isNotNull();
		assertThat(aziendaAttuale.getNome()).isEqualTo(azienda.getNome());
		assertThat(aziendaAttuale.getIndirizzo()).isEqualTo(azienda.getIndirizzo());
		assertThat(aziendaAttuale.getEmail()).isEqualTo(azienda.getEmail());
	}

}
