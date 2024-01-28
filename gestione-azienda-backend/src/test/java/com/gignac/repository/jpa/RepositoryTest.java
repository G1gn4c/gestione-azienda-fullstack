package com.gignac.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gignac.AbstractTest;
import com.gignac.entity.Azienda;
import com.gignac.entity.Impiegato;
import com.gignac.entity.Ruolo;
import com.gignac.entity.Utente;
import com.gignac.enumeration.Sesso;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RepositoryTest extends AbstractTest {
	
	private AziendaRepository aziendaRepository;
	
	private ImpiegatoRepository impiegatoRepository;
	
	private UtenteRepository utenteRepository;
	
	@Autowired
	public RepositoryTest(
			AziendaRepository aziendaRepository,
			ImpiegatoRepository impiegatoRepository,
			UtenteRepository utenteRepository
	) {
		super();
		this.aziendaRepository = aziendaRepository;
		this.impiegatoRepository = impiegatoRepository;
		this.utenteRepository = utenteRepository;
	}
	
	/**
	 * AziendaRepository
	 */

	@Test
	void testExistsByEmailAzienda() {
		String email = FAKER.internet().safeEmailAddress();
		Azienda azienda = new Azienda(
				null, 
				FAKER.company().name(), 
				FAKER.address().fullAddress(), 
				email, 
				null
		);
		aziendaRepository.save(azienda);
		boolean risultato = aziendaRepository.existsByEmail(email);
		assertThat(risultato).isTrue();
	}
	
	@Test
	void testExistsByEmailAziendaFailure() {
		String email = FAKER.internet().safeEmailAddress();
		boolean risultato = aziendaRepository.existsByEmail(email);
		assertThat(risultato).isFalse();
	}
	
	/**
	 * ImpiegatoRepository
	 */
	
	@Test
	void testExistsByEmailImpiegato() {
		Azienda azienda = new Azienda(
				null, 
				FAKER.company().name(), 
				FAKER.address().fullAddress(), 
				FAKER.internet().safeEmailAddress(), 
				null
		);
		azienda = aziendaRepository.save(azienda);
		String email = FAKER.internet().safeEmailAddress();
		Impiegato impiegato = new Impiegato(
				null, 
				FAKER.name().firstName(), 
				FAKER.name().lastName(), 
				Sesso.MASCHIO, 
				FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
				email, 
				new Azienda(azienda.getId(), null, null, null, null)
		);
		impiegatoRepository.save(impiegato);
		boolean risultato = impiegatoRepository.existsByEmail(email);
		assertThat(risultato).isTrue();
	}
	
	@Test
	void testExistsByEmailImpiegatoFailure() {
		String email = FAKER.internet().safeEmailAddress();
		boolean risultato = impiegatoRepository.existsByEmail(email);
		assertThat(risultato).isFalse();
	}
	
	/**
	 * UsernameRepository
	 */
	
	@Test
	void testExistsByUsername() {
		String username = FAKER.name().username();
		Utente utente = new Utente(
				null, 
				FAKER.name().firstName(), 
				FAKER.name().lastName(), 
				username, 
				"password", 
				new Ruolo(1L, null, null, null)
		);
		utenteRepository.save(utente);
		boolean risultato = utenteRepository.existsByUsername(username);
		assertThat(risultato).isTrue();
	}
	
	@Test
	void testExistsByUsernameFailure() {
		String username = FAKER.name().username();
		boolean risultato = utenteRepository.existsByUsername(username);
		assertThat(risultato).isFalse();
	}

}
