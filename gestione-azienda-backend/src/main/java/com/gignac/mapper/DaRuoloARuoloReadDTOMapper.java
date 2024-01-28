package com.gignac.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.gignac.dto.RuoloReadDTO;
import com.gignac.entity.Ruolo;

@Component
public class DaRuoloARuoloReadDTOMapper implements Function<Ruolo, RuoloReadDTO> {

	@Override
	public RuoloReadDTO apply(Ruolo ruolo) {
		RuoloReadDTO ruoloReadDTO = new RuoloReadDTO();
		ruoloReadDTO.setId(ruolo.getId());
		ruoloReadDTO.setCodice(ruolo.getCodice());
		ruoloReadDTO.setDescrizione(ruolo.getDescrizione());
		return ruoloReadDTO;
	}

}
