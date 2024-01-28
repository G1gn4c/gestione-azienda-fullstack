package com.gignac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gignac.dto.RuoloReadDTO;
import com.gignac.exception.RuoloNonTrovatoException;
import com.gignac.mapper.DaRuoloARuoloReadDTOMapper;
import com.gignac.repository.jpa.RuoloRepository;
import com.gignac.service.RuoloService;

@Service
public class RuoloServiceImpl implements RuoloService {
	
	private RuoloRepository ruoloRepository;
	
	private DaRuoloARuoloReadDTOMapper daRuoloARuoloReadDTOMapper;
	
	public RuoloServiceImpl(
			RuoloRepository ruoloRepository,
			DaRuoloARuoloReadDTOMapper daRuoloARuoloReadDTOMapper
	) {
		super();
		this.ruoloRepository = ruoloRepository;
		this.daRuoloARuoloReadDTOMapper = daRuoloARuoloReadDTOMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public RuoloReadDTO read(Long id) {
		return ruoloRepository.findById(id)
				              .map(ruolo -> daRuoloARuoloReadDTOMapper.apply(ruolo))
				              .orElseThrow(() -> new RuoloNonTrovatoException("Ruolo con id [%s] non trovato".formatted(id)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<RuoloReadDTO> read() {
		return ruoloRepository.findAll()
				              .stream()
				              .map(ruolo -> daRuoloARuoloReadDTOMapper.apply(ruolo))
				              .collect(Collectors.toList());
	}

}
