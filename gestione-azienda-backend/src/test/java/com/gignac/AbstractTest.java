package com.gignac;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.javafaker.Faker;

@Testcontainers
public abstract class AbstractTest {
	
	protected static final Faker FAKER = new Faker();
	
	@Container
	protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1")
			.withDatabaseName("gestione-azienda-test")
			.withUsername("gestione-azienda-test")
			.withPassword("gestione-azienda-test");
	
	@DynamicPropertySource
	private static void modificaPropertiesDatabasePerTest(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
		dynamicPropertyRegistry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
		dynamicPropertyRegistry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
		dynamicPropertyRegistry.add("spring.batch.job.enabled", () -> false);
	}
	
	@BeforeAll
	public static void migraDatabase() {
		Flyway flyway = Flyway.configure()
				.dataSource(
						postgreSQLContainer.getJdbcUrl(), 
						postgreSQLContainer.getUsername(), 
						postgreSQLContainer.getPassword()
				)
				.defaultSchema("gestione-azienda")
				.load();
		flyway.migrate();
	}
	
	private static DataSource ottieniDataSource() {
		return DataSourceBuilder.create()
				.driverClassName(postgreSQLContainer.getDriverClassName())
				.url(postgreSQLContainer.getJdbcUrl())
				.username(postgreSQLContainer.getUsername())
				.password(postgreSQLContainer.getPassword())
				.build();
	}
	
	protected static JdbcTemplate ottieniJdbcTemplate() {
		return new JdbcTemplate(ottieniDataSource());
	}
	
}
