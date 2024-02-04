package com.gignac.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gignac.dto.PaginaDTO;
import com.gignac.dto.UtenteCreateDTO;
import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.dto.UtenteUpdateDTO;
import com.gignac.entity.Ruolo;
import com.gignac.entity.Utente;
import com.gignac.exception.UsernameDuplicatoException;
import com.gignac.exception.UtenteNonTrovatoException;
import com.gignac.mapper.DaUtenteAdUtenteReadDTOMapper;
import com.gignac.repository.jdbc.UtenteJdbcRepository;
import com.gignac.repository.jpa.UtenteRepository;
import com.gignac.service.impl.UtenteServiceImpl;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {
	
	private static final Faker FAKER = new Faker();
	
	private UtenteService utenteService;
	
	@Mock
	private UtenteRepository utenteRepository;
	
	@Mock
	private UtenteJdbcRepository utenteJdbcRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() throws Exception {
		DaUtenteAdUtenteReadDTOMapper daUtenteAdUtenteReaderDTOMapper = new DaUtenteAdUtenteReadDTOMapper();
		utenteService = new UtenteServiceImpl(utenteRepository, utenteJdbcRepository, daUtenteAdUtenteReaderDTOMapper, passwordEncoder);
	}

	@Test
	void testCreate() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		String passwordCriptata = "qwertyuiop";
		UtenteCreateDTO utenteCreateDTO = new UtenteCreateDTO();
		utenteCreateDTO.setNome(nome);
		utenteCreateDTO.setCognome(cognome);
		utenteCreateDTO.setUsername(username);
		utenteCreateDTO.setPassword(password);
		utenteCreateDTO.setIdRuolo(idRuolo);
		
		when(utenteRepository.existsByUsername(username)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn(passwordCriptata);
		
		utenteService.create(utenteCreateDTO);
		
		ArgumentCaptor<Utente> argumentCaptor = ArgumentCaptor.forClass(Utente.class);
		verify(utenteRepository).save(argumentCaptor.capture());
		Utente utenteCatturato = argumentCaptor.getValue();
		
		assertThat(utenteCatturato.getId()).isNull();
		assertThat(utenteCatturato.getNome()).isEqualTo(utenteCreateDTO.getNome());
		assertThat(utenteCatturato.getCognome()).isEqualTo(utenteCreateDTO.getCognome());
		assertThat(utenteCatturato.getUsername()).isEqualTo(utenteCreateDTO.getUsername());
		assertThat(utenteCatturato.getPassword()).isEqualTo(passwordCriptata);
		assertThat(utenteCatturato.getRuolo().getId()).isEqualTo(utenteCreateDTO.getIdRuolo());
		assertThat(utenteCatturato.getRuolo().getCodice()).isNull();
		assertThat(utenteCatturato.getRuolo().getDescrizione()).isNull();
		assertThat(utenteCatturato.getRuolo().getUtenti()).isNull();
	}
	
	@Test
	void testCreateFailure() {
		String username = FAKER.name().username();
		UtenteCreateDTO utenteCreateDTO = new UtenteCreateDTO();
		utenteCreateDTO.setUsername(username);
		
		when(utenteRepository.existsByUsername(username)).thenReturn(true);

		assertThatThrownBy(() -> utenteService.create(utenteCreateDTO))
				.isInstanceOf(UsernameDuplicatoException.class)
				.hasMessage("Utente con username [%s] gia' presente".formatted(username));
		
		verify(utenteRepository, never()).save(any());
	}

	@Test
	void testReadLong() {
		Long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		String codiceRuolo = "ROLE_USER";
		String descrizioneRuolo = "Ruolo User";
		Utente utente = new Utente(
				id, 
				nome, 
				cognome, 
				username, 
				password, 
				new Ruolo(idRuolo, codiceRuolo, descrizioneRuolo, null)
		);
		
		when(utenteRepository.findById(id)).thenReturn(Optional.of(utente));
		
		UtenteReadDTO utenteAttuale = utenteService.read(id);
		
		assertThat(utenteAttuale.getId()).isEqualTo(utente.getId());
		assertThat(utenteAttuale.getNome()).isEqualTo(utente.getNome());
		assertThat(utenteAttuale.getCognome()).isEqualTo(utente.getCognome());
		assertThat(utenteAttuale.getUsername()).isEqualTo(utente.getUsername());
		assertThat(utenteAttuale.getRuolo().getId()).isEqualTo(utente.getRuolo().getId());
		assertThat(utenteAttuale.getRuolo().getCodice()).isEqualTo(codiceRuolo);
		assertThat(utenteAttuale.getRuolo().getDescrizione()).isEqualTo(descrizioneRuolo);
	}
	
	@Test
	void testReadLongFailure() {
		Long id = 1L;

		when(utenteRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> utenteService.read(id))
				.isInstanceOf(UtenteNonTrovatoException.class)
				.hasMessage("Utente con id [%s] non trovato".formatted(id));
	}

	@Test
	void testRead() {
		List<Utente> utenti = new ArrayList<>();
		Long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		String codiceRuolo = "ROLE_USER";
		String descrizioneRuolo = "Ruolo User";
		utenti.add(
				new Utente(
						id, 
						nome, 
						cognome, 
						username, 
						password, 
						new Ruolo(idRuolo, codiceRuolo, descrizioneRuolo, null)
				)
		);
		
		when(utenteRepository.findAll()).thenReturn(utenti);
		
		List<UtenteReadDTO> utentiAttuali = utenteService.read();
		
		assertThat(utentiAttuali).hasSize(1);
		assertThat(utentiAttuali.get(0).getId()).isEqualTo(id);
		assertThat(utentiAttuali.get(0).getNome()).isEqualTo(nome);
		assertThat(utentiAttuali.get(0).getCognome()).isEqualTo(cognome);
		assertThat(utentiAttuali.get(0).getUsername()).isEqualTo(username);
		assertThat(utentiAttuali.get(0).getRuolo().getId()).isEqualTo(idRuolo);
		assertThat(utentiAttuali.get(0).getRuolo().getCodice()).isEqualTo(codiceRuolo);
		assertThat(utentiAttuali.get(0).getRuolo().getDescrizione()).isEqualTo(descrizioneRuolo);
	}

	@Test
	void testUpdate() {
		long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		String passwordCriptata = "qwertyuiop";
		UtenteUpdateDTO utenteUpdateDTO = new UtenteUpdateDTO();
		utenteUpdateDTO.setId(id);
		utenteUpdateDTO.setNome(nome);
		utenteUpdateDTO.setCognome(cognome);
		utenteUpdateDTO.setUsername(username);
		utenteUpdateDTO.setPassword(password);
		utenteUpdateDTO.setIdRuolo(idRuolo);
		
		when(utenteRepository.existsById(id)).thenReturn(true);
		when(utenteRepository.existsByUsername(username)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn(passwordCriptata);
		
		utenteService.update(utenteUpdateDTO);
		
		ArgumentCaptor<Utente> argumentCaptor = ArgumentCaptor.forClass(Utente.class);
		verify(utenteRepository).save(argumentCaptor.capture());
		Utente utenteCatturato = argumentCaptor.getValue();
		
		assertThat(utenteCatturato.getId()).isEqualTo(utenteUpdateDTO.getId());
		assertThat(utenteCatturato.getNome()).isEqualTo(utenteUpdateDTO.getNome());
		assertThat(utenteCatturato.getCognome()).isEqualTo(utenteUpdateDTO.getCognome());
		assertThat(utenteCatturato.getUsername()).isEqualTo(utenteUpdateDTO.getUsername());
		assertThat(utenteCatturato.getPassword()).isEqualTo(passwordCriptata);
		assertThat(utenteCatturato.getRuolo().getId()).isEqualTo(utenteUpdateDTO.getIdRuolo());
		assertThat(utenteCatturato.getRuolo().getCodice()).isNull();
		assertThat(utenteCatturato.getRuolo().getDescrizione()).isNull();
		assertThat(utenteCatturato.getRuolo().getUtenti()).isNull();
	}
	
	@Test
	void testUpdateFailureExistById() {
		long id = 1L;
		UtenteUpdateDTO utenteUpdateDTO = new UtenteUpdateDTO();
		utenteUpdateDTO.setId(id);

		when(utenteRepository.existsById(id)).thenReturn(false);

		assertThatThrownBy(() -> utenteService.update(utenteUpdateDTO))
				.isInstanceOf(UtenteNonTrovatoException.class)
				.hasMessage("Utente con id [%s] non trovato".formatted(utenteUpdateDTO.getId()));
		
		verify(utenteRepository, never()).save(any());
	}
	
	@Test
	void testUpdateFailureExistByUsername() {
		long id = 1L;
		String username = FAKER.name().username();
		UtenteUpdateDTO utenteUpdateDTO = new UtenteUpdateDTO();
		utenteUpdateDTO.setId(id);
		utenteUpdateDTO.setUsername(username);

		when(utenteRepository.existsById(id)).thenReturn(true);
		when(utenteRepository.existsByUsername(username)).thenReturn(true);

		assertThatThrownBy(() -> utenteService.update(utenteUpdateDTO))
				.isInstanceOf(UsernameDuplicatoException.class)
				.hasMessage("Utente con username [%s] gia' presente".formatted(utenteUpdateDTO.getUsername()));
		
		verify(utenteRepository, never()).save(any());
	}

	@Test
	void testDelete() {
		long id = 1L;
		
		utenteService.delete(id);
		
		verify(utenteRepository).deleteById(id);
	}

	@Test
	void testCerca() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		UtenteFiltroDTO utenteFiltroDTO = new UtenteFiltroDTO();
		utenteFiltroDTO.setNome(nome);
		utenteFiltroDTO.setCognome(cognome);
		utenteFiltroDTO.setUsername(username);
		utenteFiltroDTO.setOrdinamento("nome");
		utenteFiltroDTO.setDirezione("ASC");
		utenteFiltroDTO.setLimite(1);
		utenteFiltroDTO.setOffset(0);
		
		List<Utente> utenti = new ArrayList<>();
		String password = "password";
		long id = 1L;
		String codiceRuolo = "ROLE_USER";
		String descrizioneRuolo = "Ruolo User";
		long count = 1L;
		utenti.add(new Utente(
				id, 
				nome, 
				cognome, 
				username, 
				password, 
				new Ruolo(id, codiceRuolo, descrizioneRuolo, null)
			  )
		);
		
		when(utenteJdbcRepository.cerca(utenteFiltroDTO)).thenReturn(utenti);
		when(utenteRepository.count()).thenReturn(count);
		
		PaginaDTO<UtenteReadDTO> paginaAttuale = utenteService.cerca(utenteFiltroDTO);
		
		assertThat(paginaAttuale.getPayload()).hasSize(1);
		assertThat(paginaAttuale.getPayload().get(0).getId()).isEqualTo(id);
		assertThat(paginaAttuale.getPayload().get(0).getNome()).isEqualTo(nome);
		assertThat(paginaAttuale.getPayload().get(0).getCognome()).isEqualTo(cognome);
		assertThat(paginaAttuale.getPayload().get(0).getUsername()).isEqualTo(username);
		assertThat(paginaAttuale.getPayload().get(0).getRuolo().getId()).isEqualTo(id);
		assertThat(paginaAttuale.getPayload().get(0).getRuolo().getCodice()).isEqualTo(codiceRuolo);
		assertThat(paginaAttuale.getPayload().get(0).getRuolo().getDescrizione()).isEqualTo(descrizioneRuolo);
		assertThat(paginaAttuale.getTotale()).isEqualTo(count);
	}

}
