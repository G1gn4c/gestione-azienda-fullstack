package com.gignac.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ImportAziendeImpiegatiJobExecutionListener implements JobExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportAziendeImpiegatiJobExecutionListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("!!! JOB AZIENDA-IMPIEGATO STA PER PARTIRE!");
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("!!! JOB AZIENDA-IMPIEGATO TERMINATO! Tempo di verificare il risultato");
		}
	}
}
