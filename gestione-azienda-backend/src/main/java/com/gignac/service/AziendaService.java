package com.gignac.service;

import java.util.List;

import com.gignac.dto.AziendaCreateDTO;
import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.dto.AziendaReadDTO;
import com.gignac.dto.AziendaUpdateDTO;
import com.gignac.dto.PaginaDTO;

public interface AziendaService {

	public void create(AziendaCreateDTO aziendaCreateDTO);
	public AziendaReadDTO read(Long id);
	public List<AziendaReadDTO> read();
	public void update(AziendaUpdateDTO aziendaUpdateDTO);
	public void delete(Long id);
	public PaginaDTO<AziendaReadDTO> cerca(AziendaFiltroDTO aziendaFiltroDTO);
	
}
