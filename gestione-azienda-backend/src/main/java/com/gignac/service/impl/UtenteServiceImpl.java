package com.gignac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gignac.dto.PaginaDTO;
import com.gignac.dto.UtenteCreateDTO;
import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.dto.UtenteUpdateDTO;
import com.gignac.entity.Ruolo;
import com.gignac.entity.Utente;
import com.gignac.exception.UsernameDuplicatoException;
import com.gignac.exception.UtenteNonTrovatoException;
import com.gignac.mapper.DaUtenteAdUtenteReadDTOMapper;
import com.gignac.repository.jdbc.UtenteJdbcRepository;
import com.gignac.repository.jpa.UtenteRepository;
import com.gignac.service.UtenteService;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	private UtenteRepository utenteRepository;
	
	private UtenteJdbcRepository utenteJdbcRepository;
	
	private DaUtenteAdUtenteReadDTOMapper daUtenteAdUtenteReaderDTOMapper;
	
	public UtenteServiceImpl(
			UtenteRepository utenteRepository,
			UtenteJdbcRepository utenteJdbcRepository,
			DaUtenteAdUtenteReadDTOMapper daUtenteAdUtenteReaderDTOMapper
	) {
		super();
		this.utenteRepository = utenteRepository;
		this.utenteJdbcRepository = utenteJdbcRepository;
		this.daUtenteAdUtenteReaderDTOMapper = daUtenteAdUtenteReaderDTOMapper;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void create(UtenteCreateDTO utenteCreateDTO) {
		if (utenteRepository.existsByUsername(utenteCreateDTO.getUsername())) {
			throw new UsernameDuplicatoException("Utente con username [%s] gia' presente".formatted(utenteCreateDTO.getUsername()));
		}
		Utente utente = new Utente(
				null, 
				utenteCreateDTO.getNome(), 
				utenteCreateDTO.getCognome(), 
				utenteCreateDTO.getUsername(), 
				utenteCreateDTO.getPassword(), 
				new Ruolo(utenteCreateDTO.getIdRuolo(), null, null, null)
		);
		utenteRepository.save(utente);
	}

	@Override
	@Transactional(readOnly = true)
	public UtenteReadDTO read(Long id) {
		return utenteRepository.findById(id)
							   .map(utente -> daUtenteAdUtenteReaderDTOMapper.apply(utente))
				               .orElseThrow(() -> new UtenteNonTrovatoException("Utente con id [%s] non trovato".formatted(id)));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UtenteReadDTO> read() {
		return utenteRepository.findAll()
				               .stream()
				               .map(utente -> daUtenteAdUtenteReaderDTOMapper.apply(utente))
				               .collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(UtenteUpdateDTO utenteUpdateDTO) {
		if (!utenteRepository.existsById(utenteUpdateDTO.getId())) {
			throw new UtenteNonTrovatoException("Utente con id [%s] non trovato".formatted(utenteUpdateDTO.getId()));
		}
		if (utenteRepository.existsByUsername(utenteUpdateDTO.getUsername())) {
			throw new UsernameDuplicatoException("Utente con username [%s] gia' presente".formatted(utenteUpdateDTO.getUsername()));
		}
		Utente utente = new Utente(
				utenteUpdateDTO.getId(), 
				utenteUpdateDTO.getNome(), 
				utenteUpdateDTO.getCognome(), 
				utenteUpdateDTO.getUsername(), 
				utenteUpdateDTO.getPassword(), 
				new Ruolo(utenteUpdateDTO.getIdRuolo(), null, null, null)
		);
		utenteRepository.save(utente);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		utenteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginaDTO<UtenteReadDTO> cerca(UtenteFiltroDTO utenteFiltroDTO) {
		PaginaDTO<UtenteReadDTO> paginaDTO = new PaginaDTO<>();
		List<UtenteReadDTO> utenti = utenteJdbcRepository.cerca(utenteFiltroDTO)
				                                         .stream()
				                                         .map(utente -> daUtenteAdUtenteReaderDTOMapper.apply(utente))
				                                         .collect(Collectors.toList());
		paginaDTO.setPayload(utenti);
		paginaDTO.setTotale(utenteRepository.count());
		return paginaDTO;
	}

}
