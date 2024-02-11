package com.gignac.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.gignac.dto.AziendaCreateDTO;

public class AziendaItemProcessor implements ItemProcessor<AziendaCreateDTO, AziendaCreateDTO> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AziendaItemProcessor.class);

	@Override
	public AziendaCreateDTO process(AziendaCreateDTO item) throws Exception {
		LOGGER.info("Sono entrato nel processor di AziendaItemProcessor");
		return item;
	}

}
