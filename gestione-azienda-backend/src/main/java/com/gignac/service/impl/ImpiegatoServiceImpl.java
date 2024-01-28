package com.gignac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gignac.dto.ImpiegatoCreateDTO;
import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.dto.ImpiegatoReadDTO;
import com.gignac.dto.ImpiegatoUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.entity.Azienda;
import com.gignac.entity.Impiegato;
import com.gignac.exception.EmailDuplicataException;
import com.gignac.exception.ImpiegatoNonTrovatoException;
import com.gignac.mapper.DaImpiegatoAdImpiegatoReadDTOMapper;
import com.gignac.repository.jdbc.ImpiegatoJdbcRepository;
import com.gignac.repository.jpa.ImpiegatoRepository;
import com.gignac.service.ImpiegatoService;

@Service
public class ImpiegatoServiceImpl implements ImpiegatoService {
	
	private ImpiegatoRepository impiegatoRepository;
	
	private ImpiegatoJdbcRepository impiegatoJdbcRepository;
	
	private DaImpiegatoAdImpiegatoReadDTOMapper daImpiegatoAdImpiegatoReadDTOMapper;
	
	public ImpiegatoServiceImpl(
			ImpiegatoRepository impiegatoRepository,
			ImpiegatoJdbcRepository impiegatoJdbcRepository,
			DaImpiegatoAdImpiegatoReadDTOMapper daImpiegatoAdImpiegatoReadDTOMapper
	) {
		super();
		this.impiegatoRepository = impiegatoRepository;
		this.impiegatoJdbcRepository = impiegatoJdbcRepository;
		this.daImpiegatoAdImpiegatoReadDTOMapper = daImpiegatoAdImpiegatoReadDTOMapper;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void create(ImpiegatoCreateDTO impiegatoCreateDTO) {
		if (impiegatoRepository.existsByEmail(impiegatoCreateDTO.getEmail())) {
			throw new EmailDuplicataException("Impiegato con email [%s] gia' presente".formatted(impiegatoCreateDTO.getEmail()));
		}
		Impiegato impiegato = new Impiegato(
				null, 
				impiegatoCreateDTO.getNome(), 
				impiegatoCreateDTO.getCognome(), 
				impiegatoCreateDTO.getSesso(), 
				impiegatoCreateDTO.getDataNascita(), 
				impiegatoCreateDTO.getEmail(), 
				new Azienda(impiegatoCreateDTO.getIdAzienda(), null, null, null, null)
		);
		impiegatoRepository.save(impiegato);
	}

	@Override
	@Transactional(readOnly = true)
	public ImpiegatoReadDTO read(Long id) {
		return impiegatoRepository.findById(id)
				                  .map(impiegato -> daImpiegatoAdImpiegatoReadDTOMapper.apply(impiegato))
				                  .orElseThrow(() -> new ImpiegatoNonTrovatoException("Impiegato con id [%s] non trovato".formatted(id)));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ImpiegatoReadDTO> read() {
		return impiegatoRepository.findAll()
				                  .stream()
				                  .map(impiegato -> daImpiegatoAdImpiegatoReadDTOMapper.apply(impiegato))
				                  .collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ImpiegatoUpdateDTO impiegatoUpdateDTO) {
		if (!impiegatoRepository.existsById(impiegatoUpdateDTO.getId())) {
			throw new ImpiegatoNonTrovatoException("Impiegato con id [%s] non trovato".formatted(impiegatoUpdateDTO.getId()));
		}
		if (impiegatoRepository.existsByEmail(impiegatoUpdateDTO.getEmail())) {
			throw new EmailDuplicataException("Impiegato con email [%s] gia' presente".formatted(impiegatoUpdateDTO.getEmail()));
		}
		Impiegato impiegato = new Impiegato(
				impiegatoUpdateDTO.getId(), 
				impiegatoUpdateDTO.getNome(), 
				impiegatoUpdateDTO.getCognome(), 
				impiegatoUpdateDTO.getSesso(), 
				impiegatoUpdateDTO.getDataNascita(),  
				impiegatoUpdateDTO.getEmail(), 
				new Azienda(impiegatoUpdateDTO.getIdAzienda(), null, null, null, null)
		);
		impiegatoRepository.save(impiegato);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		impiegatoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginaDTO<ImpiegatoReadDTO> cerca(ImpiegatoFiltroDTO impiegatoFiltroDTO) {
		PaginaDTO<ImpiegatoReadDTO> paginaDTO = new PaginaDTO<>();
		List<ImpiegatoReadDTO> impiegati = impiegatoJdbcRepository.cerca(impiegatoFiltroDTO)
				                                                  .stream()
				                                                  .map(impiegato -> daImpiegatoAdImpiegatoReadDTOMapper.apply(impiegato))
				                				                  .collect(Collectors.toList());;
		paginaDTO.setPayload(impiegati);
		paginaDTO.setTotale(impiegatoRepository.count());
		return paginaDTO;
	}

}
