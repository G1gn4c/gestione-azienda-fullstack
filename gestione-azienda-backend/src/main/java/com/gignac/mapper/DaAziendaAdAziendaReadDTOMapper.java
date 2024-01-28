package com.gignac.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.gignac.dto.AziendaReadDTO;
import com.gignac.entity.Azienda;

@Component
public class DaAziendaAdAziendaReadDTOMapper implements Function<Azienda, AziendaReadDTO> {

	@Override
	public AziendaReadDTO apply(Azienda azienda) {
		AziendaReadDTO aziendaReadDTO = new AziendaReadDTO();
		aziendaReadDTO.setId(azienda.getId());
		aziendaReadDTO.setNome(azienda.getNome());
		aziendaReadDTO.setIndirizzo(azienda.getIndirizzo());
		aziendaReadDTO.setEmail(azienda.getEmail());
		return aziendaReadDTO;
	}

}
