package com.gignac.service;

import java.util.List;

import com.gignac.dto.ImpiegatoCreateDTO;
import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.dto.ImpiegatoReadDTO;
import com.gignac.dto.ImpiegatoUpdateDTO;
import com.gignac.dto.PaginaDTO;

public interface ImpiegatoService {

	public void create(ImpiegatoCreateDTO impiegatoCreateDTO);
	public ImpiegatoReadDTO read(Long id);
	public List<ImpiegatoReadDTO> read();
	public void update(ImpiegatoUpdateDTO impiegatoUpdateDTO);
	public void delete(Long id);
	public PaginaDTO<ImpiegatoReadDTO> cerca(ImpiegatoFiltroDTO impiegatoFiltroDTO);
	
}
