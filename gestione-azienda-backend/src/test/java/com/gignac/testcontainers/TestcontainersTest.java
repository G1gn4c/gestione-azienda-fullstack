package com.gignac.testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gignac.AbstractTest;

public class TestcontainersTest extends AbstractTest {
	
	@Test
	public void verificaPostgresContainer() {
		assertThat(postgreSQLContainer.isRunning()).isTrue();
		assertThat(postgreSQLContainer.isCreated()).isTrue();
	}

}
