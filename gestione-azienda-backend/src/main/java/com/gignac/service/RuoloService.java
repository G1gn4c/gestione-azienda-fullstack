package com.gignac.service;

import java.util.List;

import com.gignac.dto.RuoloReadDTO;

public interface RuoloService {
	
	public RuoloReadDTO read(Long id);
	public List<RuoloReadDTO> read();

}
