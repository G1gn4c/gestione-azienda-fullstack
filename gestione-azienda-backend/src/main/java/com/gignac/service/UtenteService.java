package com.gignac.service;

import java.util.List;

import com.gignac.dto.PaginaDTO;
import com.gignac.dto.UtenteCreateDTO;
import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.dto.UtenteUpdateDTO;

public interface UtenteService {
	
	public void create(UtenteCreateDTO utenteCreateDTO);
	public UtenteReadDTO read(Long id);
	public List<UtenteReadDTO> read();
	public void update(UtenteUpdateDTO utenteUpdateDTO);
	public void delete(Long id);
	public PaginaDTO<UtenteReadDTO> cerca(UtenteFiltroDTO utenteFiltroDTO);
	
}
