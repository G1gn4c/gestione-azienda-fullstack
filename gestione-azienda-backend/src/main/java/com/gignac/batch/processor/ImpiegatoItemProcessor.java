package com.gignac.batch.processor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.gignac.dto.ImpiegatoCreateBatchReadDTO;
import com.gignac.dto.ImpiegatoCreateBatchWriteDTO;

public class ImpiegatoItemProcessor implements ItemProcessor<ImpiegatoCreateBatchReadDTO, ImpiegatoCreateBatchWriteDTO> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImpiegatoItemProcessor.class);

	@Override
	public ImpiegatoCreateBatchWriteDTO process(ImpiegatoCreateBatchReadDTO item) throws Exception {
		LOGGER.info("Sono entrato nel processor di ImpiegatoItemProcessor");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascitaDate = simpleDateFormat.parse(item.getDataNascita());
		LocalDate dataNascitaLocalDate = dataNascitaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		ImpiegatoCreateBatchWriteDTO impiegatoCreateBatchWriteDTO = new ImpiegatoCreateBatchWriteDTO();
		impiegatoCreateBatchWriteDTO.setNome(item.getNome());
		impiegatoCreateBatchWriteDTO.setCognome(item.getCognome());
		impiegatoCreateBatchWriteDTO.setSesso(item.getSesso().name());
		impiegatoCreateBatchWriteDTO.setEmail(item.getEmail());
		impiegatoCreateBatchWriteDTO.setIdAzienda(item.getIdAzienda());
		impiegatoCreateBatchWriteDTO.setDataNascita(dataNascitaLocalDate);
		return impiegatoCreateBatchWriteDTO;
	}

}
