package com.gignac.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.gignac.dto.AziendaReadDTO;
import com.gignac.dto.ImpiegatoReadDTO;
import com.gignac.entity.Impiegato;

@Component
public class DaImpiegatoAdImpiegatoReadDTOMapper implements Function<Impiegato, ImpiegatoReadDTO> {

	@Override
	public ImpiegatoReadDTO apply(Impiegato impiegato) {
		ImpiegatoReadDTO impiegatoReadDTO = new ImpiegatoReadDTO();
		impiegatoReadDTO.setId(impiegato.getId());
		impiegatoReadDTO.setNome(impiegato.getNome());
		impiegatoReadDTO.setCognome(impiegato.getCognome());
		impiegatoReadDTO.setSesso(impiegato.getSesso());
		impiegatoReadDTO.setDataNascita(impiegato.getDataNascita());
		impiegatoReadDTO.setEmail(impiegato.getEmail());

		AziendaReadDTO aziendaReadDTO = new AziendaReadDTO();
		aziendaReadDTO.setId(impiegato.getAzienda().getId());
		aziendaReadDTO.setNome(impiegato.getAzienda().getNome());
		aziendaReadDTO.setIndirizzo(impiegato.getAzienda().getIndirizzo());
		aziendaReadDTO.setEmail(impiegato.getAzienda().getEmail());

		impiegatoReadDTO.setAzienda(aziendaReadDTO);
		return impiegatoReadDTO;
	}

}
