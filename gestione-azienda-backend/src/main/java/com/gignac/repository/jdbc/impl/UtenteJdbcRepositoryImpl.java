package com.gignac.repository.jdbc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.entity.Utente;
import com.gignac.repository.jdbc.UtenteJdbcRepository;
import com.gignac.repository.jdbc.rowmapper.UtenteRowMapper;

@Repository
public class UtenteJdbcRepositoryImpl implements UtenteJdbcRepository {
	
	private static final Logger log = LoggerFactory.getLogger(UtenteJdbcRepositoryImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private static final UtenteRowMapper ROW_MAPPER = new UtenteRowMapper();
	
	public UtenteJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Utente> cerca(UtenteFiltroDTO utenteFiltroDTO) {
		StringBuilder sql = new StringBuilder();
		List<Object> parametri = new ArrayList<>();
		sql.append("SELECT * ")
		   .append("FROM \"gestione-azienda\".utente ");
		String separatore = "WHERE ";
		if (utenteFiltroDTO != null && utenteFiltroDTO.getNome() != null && !utenteFiltroDTO.getNome().isBlank()) {
			sql.append(separatore)
			   .append("nome ilike ? ");
			parametri.add("%" + utenteFiltroDTO.getNome() + "%");
			separatore = "AND ";
		}
		if (utenteFiltroDTO != null && utenteFiltroDTO.getCognome() != null && !utenteFiltroDTO.getCognome().isBlank()) {
			sql.append(separatore)
			   .append("cognome ilike ? ");
			parametri.add("%" + utenteFiltroDTO.getCognome() + "%");
			separatore = "AND ";
		}
		if (utenteFiltroDTO != null && utenteFiltroDTO.getUsername() != null && !utenteFiltroDTO.getUsername().isBlank()) {
			sql.append(separatore)
			   .append("username ilike ? ");
			parametri.add("%" + utenteFiltroDTO.getUsername() + "%");
			separatore = "AND ";
		}
		
		if (utenteFiltroDTO.getOrdinamento().equals("nome")) {
			sql.append("ORDER BY nome ");
			if (utenteFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (utenteFiltroDTO.getOrdinamento().equals("cognome")) {
			sql.append("ORDER BY cognome ");
			if (utenteFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (utenteFiltroDTO.getOrdinamento().equals("username")) {
			sql.append("ORDER BY username ");
			if (utenteFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		}
		sql.append("LIMIT ? ");
		parametri.add(utenteFiltroDTO.getLimite());
		sql.append("OFFSET ? ");
		parametri.add(utenteFiltroDTO.getOffset());
		log.info("Quesry: {}", sql);
		log.info("Parametri: {}", parametri);
		return jdbcTemplate.query(sql.toString(), parametri.toArray(), ROW_MAPPER);
	}

}
