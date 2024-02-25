package com.gignac.mapper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gignac.dto.AziendaJasperDTO;
import com.gignac.dto.ImpiegatoJasperDTO;
import com.gignac.entity.Azienda;

@Component
public class DaAziendaAdAziendaJasperDTOMapper implements Function<Azienda, AziendaJasperDTO> {

	@Override
	public AziendaJasperDTO apply(Azienda azienda) {
		AziendaJasperDTO aziendaJasperDTO = new AziendaJasperDTO();
		aziendaJasperDTO.setNome(azienda.getNome());
		aziendaJasperDTO.setIndirizzo(azienda.getIndirizzo());
		aziendaJasperDTO.setEmail(azienda.getEmail());
		
		List<ImpiegatoJasperDTO> impiegati = azienda.getImpiegati()
		                                            .stream()
		                                            .map(impiegato -> {
		    	                                        ImpiegatoJasperDTO impiegatoJasperDTO = new ImpiegatoJasperDTO();
		    	                                        impiegatoJasperDTO.setNome(impiegato.getNome());
		    	                                        impiegatoJasperDTO.setCognome(impiegato.getCognome());
		    	                                        impiegatoJasperDTO.setSesso(impiegato.getSesso().name());
		    	                                        impiegatoJasperDTO.setEmail(impiegato.getEmail());
		    	                                        
		    	                                        Date dataNascitaDate = java.sql.Date.valueOf(impiegato.getDataNascita());
		    	                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		    	                                        impiegatoJasperDTO.setDataNascita(simpleDateFormat.format(dataNascitaDate));

		    	                                        return impiegatoJasperDTO;
		                                            })
		                                            .collect(Collectors.toList());
		
		aziendaJasperDTO.setImpiegati(impiegati);
		return aziendaJasperDTO;
	}

}
