package com.gignac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gignac.dto.AziendaCreateDTO;
import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.dto.AziendaReadDTO;
import com.gignac.dto.AziendaUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.entity.Azienda;
import com.gignac.exception.AziendaNonTrovataException;
import com.gignac.exception.EmailDuplicataException;
import com.gignac.mapper.DaAziendaAdAziendaReadDTOMapper;
import com.gignac.repository.jdbc.AziendaJdbcRepository;
import com.gignac.repository.jpa.AziendaRepository;
import com.gignac.service.AziendaService;

@Service
public class AziendaServiceImpl implements AziendaService {
	
	private AziendaRepository aziendaRepository;
	
	private AziendaJdbcRepository aziendaJdbcRepository;
	
	private DaAziendaAdAziendaReadDTOMapper daAziendaAdAziendaReadDTOMapper;
	
	public AziendaServiceImpl(
			AziendaRepository aziendaRepository,
			AziendaJdbcRepository aziendaJdbcRepository,
			DaAziendaAdAziendaReadDTOMapper daAziendaAdAziendaReadDTOMapper
	) {
		super();
		this.aziendaRepository = aziendaRepository;
		this.aziendaJdbcRepository = aziendaJdbcRepository;
		this.daAziendaAdAziendaReadDTOMapper = daAziendaAdAziendaReadDTOMapper;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void create(AziendaCreateDTO aziendaCreateDTO) {
		if (aziendaRepository.existsByEmail(aziendaCreateDTO.getEmail())) {
			throw new EmailDuplicataException("Azienda con email [%s] gia' presente".formatted(aziendaCreateDTO.getEmail()));
		}
		Azienda azienda = new Azienda(
				null, 
				aziendaCreateDTO.getNome(), 
				aziendaCreateDTO.getIndirizzo(), 
				aziendaCreateDTO.getEmail(), 
				null
		);
		aziendaRepository.save(azienda);
	}

	@Override
	@Transactional(readOnly = true)
	public AziendaReadDTO read(Long id) {
		return aziendaRepository.findById(id)
				                .map(azienda -> daAziendaAdAziendaReadDTOMapper.apply(azienda))
				                .orElseThrow(() -> new AziendaNonTrovataException("Azienda con id [%s] non trovata".formatted(id)));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AziendaReadDTO> read() {
		return aziendaRepository.findAll()
				                .stream()
				                .map(azienda -> daAziendaAdAziendaReadDTOMapper.apply(azienda))
				                .collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(AziendaUpdateDTO aziendaUpdateDTO) {
		if (!aziendaRepository.existsById(aziendaUpdateDTO.getId())) {
			throw new AziendaNonTrovataException("Azienda con id [%s] non trovata".formatted(aziendaUpdateDTO.getId()));
		}
		if (aziendaRepository.existsByEmail(aziendaUpdateDTO.getEmail())) {
			throw new EmailDuplicataException("Azienda con email [%s] gia' presente".formatted(aziendaUpdateDTO.getEmail()));
		}
		Azienda azienda = new Azienda(
				aziendaUpdateDTO.getId(), 
				aziendaUpdateDTO.getNome(), 
				aziendaUpdateDTO.getIndirizzo(), 
				aziendaUpdateDTO.getEmail(), 
				null
		);
		aziendaRepository.save(azienda);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		aziendaRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginaDTO<AziendaReadDTO> cerca(AziendaFiltroDTO aziendaFiltroDTO) {
		PaginaDTO<AziendaReadDTO> paginaDTO = new PaginaDTO<>();
		List<AziendaReadDTO> aziende = aziendaJdbcRepository.cerca(aziendaFiltroDTO)
				                                            .stream()
				                                            .map(azienda -> daAziendaAdAziendaReadDTOMapper.apply(azienda))
				                                            .collect(Collectors.toList());
		paginaDTO.setPayload(aziende);
		paginaDTO.setTotale(aziendaRepository.count());
		return paginaDTO;
	}

}
