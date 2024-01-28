package com.gignac.repository.jdbc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.entity.Impiegato;
import com.gignac.repository.jdbc.ImpiegatoJdbcRepository;
import com.gignac.repository.jdbc.rowmapper.ImpiegatoRowMapper;

@Repository
public class ImpiegatoJdbcRepositoryImpl implements ImpiegatoJdbcRepository {
	
	private static final Logger log = LoggerFactory.getLogger(ImpiegatoJdbcRepositoryImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private static final ImpiegatoRowMapper ROW_MAPPER = new ImpiegatoRowMapper();
	
	public ImpiegatoJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Impiegato> cerca(ImpiegatoFiltroDTO impiegatoFiltroDTO) {
		StringBuilder sql = new StringBuilder();
		List<Object> parametri = new ArrayList<>();
		sql.append("SELECT * ")
		   .append("FROM \"gestione-azienda\".impiegato ");
		String separatore = "WHERE ";
		if (impiegatoFiltroDTO != null && impiegatoFiltroDTO.getNome() != null && !impiegatoFiltroDTO.getNome().isBlank()) {
			sql.append(separatore)
			   .append("nome ilike ? ");
			parametri.add("%" + impiegatoFiltroDTO.getNome() + "%");
			separatore = "AND ";
		}
		if (impiegatoFiltroDTO != null && impiegatoFiltroDTO.getCognome() != null && !impiegatoFiltroDTO.getCognome().isBlank()) {
			sql.append(separatore)
			   .append("cognome ilike ? ");
			parametri.add("%" + impiegatoFiltroDTO.getCognome() + "%");
			separatore = "AND ";
		}
		if (impiegatoFiltroDTO != null && impiegatoFiltroDTO.getSesso() != null) {
			sql.append(separatore)
			   .append("sesso = ? ");
			parametri.add(impiegatoFiltroDTO.getSesso().name());
			separatore = "AND ";
		}
		if (impiegatoFiltroDTO != null && impiegatoFiltroDTO.getDataNascitaDa() != null) {
			sql.append(separatore)
			   .append("data_nascita >= ? ");
			parametri.add(impiegatoFiltroDTO.getDataNascitaDa());
			separatore = "AND ";
		}
		if (impiegatoFiltroDTO != null && impiegatoFiltroDTO.getDataNascitaA() != null) {
			sql.append(separatore)
			   .append("data_nascita <= ? ");
			parametri.add(impiegatoFiltroDTO.getDataNascitaA());
			separatore = "AND ";
		}
		if (impiegatoFiltroDTO != null && impiegatoFiltroDTO.getEmail() != null && !impiegatoFiltroDTO.getEmail().isBlank()) {
			sql.append(separatore)
			   .append("email ilike ? ");
			parametri.add("%" + impiegatoFiltroDTO.getEmail() + "%");
			separatore = "AND ";
		}
		
		if (impiegatoFiltroDTO.getOrdinamento().equals("nome")) {
			sql.append("ORDER BY nome ");
			if (impiegatoFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (impiegatoFiltroDTO.getOrdinamento().equals("cognome")) {
			sql.append("ORDER BY cognome ");
			if (impiegatoFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (impiegatoFiltroDTO.getOrdinamento().equals("sesso")) {
			sql.append("ORDER BY sesso ");
			if (impiegatoFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (impiegatoFiltroDTO.getOrdinamento().equals("data_nascita")) {
			sql.append("ORDER BY data_nascita ");
			if (impiegatoFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (impiegatoFiltroDTO.getOrdinamento().equals("email")) {
			sql.append("ORDER BY email ");
			if (impiegatoFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} 
		sql.append("LIMIT ? ");
		parametri.add(impiegatoFiltroDTO.getLimite());
		sql.append("OFFSET ? ");
		parametri.add(impiegatoFiltroDTO.getOffset());
		log.info("Quesry: {}", sql);
		log.info("Parametri: {}", parametri);
		return jdbcTemplate.query(sql.toString(), parametri.toArray(), ROW_MAPPER);
	}

}
