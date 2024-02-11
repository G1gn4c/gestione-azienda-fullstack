package com.gignac.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.gignac.batch.listener.ImportAziendeImpiegatiJobExecutionListener;
import com.gignac.batch.processor.AziendaItemProcessor;
import com.gignac.dto.AziendaCreateDTO;

@Configuration
public class BatchConfig {

	@Bean
	public FlatFileItemReader<AziendaCreateDTO> aziendaItemReader() {
		return new FlatFileItemReaderBuilder<AziendaCreateDTO>()
				.name("aziendaItemReader")
				.resource(new ClassPathResource("batch/aziende.csv"))
				.delimited()
				.names("nome", "indirizzo", "email")
				.targetType(AziendaCreateDTO.class)
				.build();
	}

	@Bean
	public AziendaItemProcessor aziendaItemProcessor() {
		return new AziendaItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<AziendaCreateDTO> aziendaItemWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<AziendaCreateDTO>()
				.sql("INSERT INTO \"gestione-azienda\".azienda (id, email, indirizzo, nome) VALUES (nextval('\"gestione-azienda\".azienda_id_seq'), :email, :indirizzo, :nome)")
				.dataSource(dataSource)
				.beanMapped()
				.build();
	}
	
	@Bean
	public Step importAziendeStep(
			JobRepository jobRepository, 
			PlatformTransactionManager transactionManager,
			FlatFileItemReader<AziendaCreateDTO> aziendaItemReader, 
			AziendaItemProcessor aziendaItemProcessor,
			JdbcBatchItemWriter<AziendaCreateDTO> aziendaItemWriter
	) {
		return new StepBuilder("importAziendeStep", jobRepository)
				.<AziendaCreateDTO, AziendaCreateDTO>chunk(3, transactionManager)
				.reader(aziendaItemReader)
				.processor(aziendaItemProcessor)
				.writer(aziendaItemWriter)
				.build();
	}

	@Bean
	public Job importAziendeImpiegatiJob(
			JobRepository jobRepository, 
			Step importAziendeStep,
			ImportAziendeImpiegatiJobExecutionListener importAziendeImpiegatiJobExecutionListener
	) {
		return new JobBuilder("importAziendeImpiegatiJob", jobRepository)
				.listener(importAziendeImpiegatiJobExecutionListener)
				.start(importAziendeStep)
				.build();
	}

}
