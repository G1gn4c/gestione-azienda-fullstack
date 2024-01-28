package com.gignac.repository.jdbc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.entity.Azienda;
import com.gignac.repository.jdbc.AziendaJdbcRepository;
import com.gignac.repository.jdbc.rowmapper.AziendaRowMapper;

@Repository
public class AziendaJdbcRepositoryImpl implements AziendaJdbcRepository {
	
	private static final Logger log = LoggerFactory.getLogger(AziendaJdbcRepositoryImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private static final AziendaRowMapper ROW_MAPPER = new AziendaRowMapper();
	
	public AziendaJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Azienda> cerca(AziendaFiltroDTO aziendaFiltroDTO) {
		StringBuilder sql = new StringBuilder();
		List<Object> parametri = new ArrayList<>();
		sql.append("SELECT * ")
		   .append("FROM \"gestione-azienda\".azienda ");
		String separatore = "WHERE ";
		if (aziendaFiltroDTO != null && aziendaFiltroDTO.getNome() != null && !aziendaFiltroDTO.getNome().isBlank()) {
			sql.append(separatore)
			   .append("nome ilike ? ");
			parametri.add("%" + aziendaFiltroDTO.getNome() + "%");
			separatore = "AND ";
		}
		if (aziendaFiltroDTO != null && aziendaFiltroDTO.getIndirizzo() != null && !aziendaFiltroDTO.getIndirizzo().isBlank()) {
			sql.append(separatore)
			   .append("indirizzo ilike ? ");
			parametri.add("%" + aziendaFiltroDTO.getIndirizzo() + "%");
			separatore = "AND ";
		}
		if (aziendaFiltroDTO != null && aziendaFiltroDTO.getEmail() != null && !aziendaFiltroDTO.getEmail().isBlank()) {
			sql.append(separatore)
			   .append("email ilike ? ");
			parametri.add("%" + aziendaFiltroDTO.getEmail() + "%");
			separatore = "AND ";
		}
		
		if (aziendaFiltroDTO.getOrdinamento().equals("nome")) {
			sql.append("ORDER BY nome ");
			if (aziendaFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (aziendaFiltroDTO.getOrdinamento().equals("indirizzo")) {
			sql.append("ORDER BY indirizzo ");
			if (aziendaFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		} else if (aziendaFiltroDTO.getOrdinamento().equals("email")) {
			sql.append("ORDER BY email ");
			if (aziendaFiltroDTO.getDirezione().equals("ASC")) {
				sql.append("ASC ");	
			} else {
				sql.append("DESC ");
			}
		}
		sql.append("LIMIT ? ");
		parametri.add(aziendaFiltroDTO.getLimite());
		sql.append("OFFSET ? ");
		parametri.add(aziendaFiltroDTO.getOffset());
		log.info("Quesry: {}", sql);
		log.info("Parametri: {}", parametri);
		return jdbcTemplate.query(sql.toString(), parametri.toArray(), ROW_MAPPER);
	}

}
