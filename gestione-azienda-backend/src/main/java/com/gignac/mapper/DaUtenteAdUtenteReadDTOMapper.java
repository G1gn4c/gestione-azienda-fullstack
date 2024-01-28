package com.gignac.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.gignac.dto.RuoloReadDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.entity.Utente;

@Component
public class DaUtenteAdUtenteReadDTOMapper implements Function<Utente, UtenteReadDTO> {

	@Override
	public UtenteReadDTO apply(Utente utente) {
		UtenteReadDTO utenteReadDTO = new UtenteReadDTO();
		utenteReadDTO.setId(utente.getId());
		utenteReadDTO.setNome(utente.getNome());
		utenteReadDTO.setCognome(utente.getCognome());
		utenteReadDTO.setUsername(utente.getUsername());

		RuoloReadDTO ruoloReadDTO = new RuoloReadDTO();
		ruoloReadDTO.setId(utente.getRuolo().getId());
		ruoloReadDTO.setCodice(utente.getRuolo().getCodice());
		ruoloReadDTO.setDescrizione(utente.getRuolo().getDescrizione());

		utenteReadDTO.setRuolo(ruoloReadDTO);
		return utenteReadDTO;
	}

}
