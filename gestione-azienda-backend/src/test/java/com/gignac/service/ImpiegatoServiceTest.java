package com.gignac.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gignac.dto.ImpiegatoCreateDTO;
import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.dto.ImpiegatoReadDTO;
import com.gignac.dto.ImpiegatoUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.entity.Azienda;
import com.gignac.entity.Impiegato;
import com.gignac.enumeration.Sesso;
import com.gignac.exception.EmailDuplicataException;
import com.gignac.exception.ImpiegatoNonTrovatoException;
import com.gignac.mapper.DaImpiegatoAdImpiegatoReadDTOMapper;
import com.gignac.repository.jdbc.ImpiegatoJdbcRepository;
import com.gignac.repository.jpa.ImpiegatoRepository;
import com.gignac.service.impl.ImpiegatoServiceImpl;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ImpiegatoServiceTest {
	
	private static final Faker FAKER = new Faker();

	private ImpiegatoService impiegatoService;
	
	private DaImpiegatoAdImpiegatoReadDTOMapper daImpiegatoAdImpiegatoReadDTOMapper;

	@Mock
	private ImpiegatoRepository impiegatoRepository;

	@Mock
	private ImpiegatoJdbcRepository impiegatoJdbcRepository;

	@BeforeEach
	void setUp() throws Exception {
		daImpiegatoAdImpiegatoReadDTOMapper = new DaImpiegatoAdImpiegatoReadDTOMapper();
		impiegatoService = new ImpiegatoServiceImpl(impiegatoRepository, impiegatoJdbcRepository, daImpiegatoAdImpiegatoReadDTOMapper);
	}

	@Test
	void testCreate() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		Sesso sesso = Sesso.MASCHIO;
		String email = FAKER.internet().safeEmailAddress();
		long idAzienda = 1L;
		LocalDate dataNascita = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ImpiegatoCreateDTO impiegatoCreateDTO = new ImpiegatoCreateDTO();
		impiegatoCreateDTO.setNome(nome);
		impiegatoCreateDTO.setCognome(cognome);
		impiegatoCreateDTO.setSesso(sesso);
		impiegatoCreateDTO.setEmail(email);
		impiegatoCreateDTO.setIdAzienda(idAzienda);
		impiegatoCreateDTO.setDataNascita(dataNascita);
		
		when(impiegatoRepository.existsByEmail(impiegatoCreateDTO.getEmail())).thenReturn(false);
		
		impiegatoService.create(impiegatoCreateDTO);
		
		ArgumentCaptor<Impiegato> argumentCaptor = ArgumentCaptor.forClass(Impiegato.class);
		verify(impiegatoRepository).save(argumentCaptor.capture());
		Impiegato impiegatoCatturato = argumentCaptor.getValue();
		
		assertThat(impiegatoCatturato.getId()).isNull();
		assertThat(impiegatoCatturato.getNome()).isEqualTo(impiegatoCreateDTO.getNome());
		assertThat(impiegatoCatturato.getCognome()).isEqualTo(impiegatoCreateDTO.getCognome());
		assertThat(impiegatoCatturato.getSesso()).isEqualTo(impiegatoCreateDTO.getSesso());
		assertThat(impiegatoCatturato.getDataNascita()).isEqualTo(impiegatoCreateDTO.getDataNascita());
		assertThat(impiegatoCatturato.getEmail()).isEqualTo(impiegatoCreateDTO.getEmail());
		assertThat(impiegatoCatturato.getAzienda().getId()).isEqualTo(impiegatoCreateDTO.getIdAzienda());
		assertThat(impiegatoCatturato.getAzienda().getNome()).isNull();
		assertThat(impiegatoCatturato.getAzienda().getIndirizzo()).isNull();
		assertThat(impiegatoCatturato.getAzienda().getEmail()).isNull();
		assertThat(impiegatoCatturato.getAzienda().getImpiegati()).isNull();
	}
	
	@Test
	void testCreateFailure() {
		String email = FAKER.internet().safeEmailAddress();
		ImpiegatoCreateDTO impiegatoCreateDTO = new ImpiegatoCreateDTO();
		impiegatoCreateDTO.setEmail(email);
		
		when(impiegatoRepository.existsByEmail(impiegatoCreateDTO.getEmail())).thenReturn(true);

		assertThatThrownBy(() -> impiegatoService.create(impiegatoCreateDTO))
				.isInstanceOf(EmailDuplicataException.class)
				.hasMessage("Impiegato con email [%s] gia' presente".formatted(impiegatoCreateDTO.getEmail()));

		verify(impiegatoRepository, never()).save(any());
	}

	@Test
	void testReadLong() {
		long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		Sesso sesso = Sesso.MASCHIO;
		String email = FAKER.internet().safeEmailAddress();
		long idAzienda = 1L;
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		LocalDate dataNascita = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Impiegato impiegato = new Impiegato(
				id, 
				nome, 
				cognome, 
				sesso, 
				dataNascita, 
				email, 
				new Azienda(idAzienda, nomeAzienda, indirizzoAzienda, emailAzienda, null)
		);
		
		when(impiegatoRepository.findById(id)).thenReturn(Optional.of(impiegato));
		
		ImpiegatoReadDTO impiegatoAttuale = impiegatoService.read(id);
		
		assertThat(impiegatoAttuale.getId()).isEqualTo(impiegato.getId());
		assertThat(impiegatoAttuale.getNome()).isEqualTo(impiegato.getNome());
		assertThat(impiegatoAttuale.getCognome()).isEqualTo(impiegato.getCognome());
		assertThat(impiegatoAttuale.getSesso()).isEqualTo(impiegato.getSesso());
		assertThat(impiegatoAttuale.getDataNascita()).isEqualTo(impiegato.getDataNascita());
		assertThat(impiegatoAttuale.getEmail()).isEqualTo(impiegato.getEmail());
		assertThat(impiegatoAttuale.getAzienda().getId()).isEqualTo(impiegato.getAzienda().getId());
		assertThat(impiegatoAttuale.getAzienda().getNome()).isEqualTo(impiegato.getAzienda().getNome());
		assertThat(impiegatoAttuale.getAzienda().getIndirizzo()).isEqualTo(impiegato.getAzienda().getIndirizzo());
		assertThat(impiegatoAttuale.getAzienda().getEmail()).isEqualTo(impiegato.getAzienda().getEmail());
	}
	
	@Test
	void testReadLongFailure() {
		long id = 1L;
		
		when(impiegatoRepository.findById(id)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> impiegatoService.read(id))
				.isInstanceOf(ImpiegatoNonTrovatoException.class)
				.hasMessage("Impiegato con id [%s] non trovato".formatted(id));
	}

	@Test
	void testRead() {
		List<Impiegato> impiegati = new ArrayList<>();
		long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		Sesso sesso = Sesso.MASCHIO;
		String email = FAKER.internet().safeEmailAddress();
		LocalDate dataNascita = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long idAzienda = 1L;
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		impiegati.add(
				new Impiegato(
						id, 
						nome, 
						cognome, 
						sesso, 
						dataNascita, 
						email,
						new Azienda(idAzienda, nomeAzienda, indirizzoAzienda, emailAzienda, null)
				)
		);
		
		when(impiegatoRepository.findAll()).thenReturn(impiegati);
		
		List<ImpiegatoReadDTO> impiegatiAttuali = impiegatoService.read();
		
		assertThat(impiegatiAttuali).hasSize(1);
		assertThat(impiegatiAttuali.get(0).getId()).isEqualTo(id);
		assertThat(impiegatiAttuali.get(0).getNome()).isEqualTo(nome);
		assertThat(impiegatiAttuali.get(0).getCognome()).isEqualTo(cognome);
		assertThat(impiegatiAttuali.get(0).getSesso()).isEqualTo(sesso);
		assertThat(impiegatiAttuali.get(0).getDataNascita()).isEqualTo(dataNascita);
		assertThat(impiegatiAttuali.get(0).getEmail()).isEqualTo(email);
		assertThat(impiegatiAttuali.get(0).getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(impiegatiAttuali.get(0).getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(impiegatiAttuali.get(0).getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(impiegatiAttuali.get(0).getAzienda().getEmail()).isEqualTo(emailAzienda);
	}

	@Test
	void testUpdate() {
		long id = 1L;
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		Sesso sesso = Sesso.MASCHIO;
		String email = FAKER.internet().safeEmailAddress();
		LocalDate dataNascita = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long idAzienda = 1L;
		ImpiegatoUpdateDTO impiegatoUpdateDTO = new ImpiegatoUpdateDTO();
		impiegatoUpdateDTO.setId(id);
		impiegatoUpdateDTO.setNome(nome);
		impiegatoUpdateDTO.setCognome(cognome);
		impiegatoUpdateDTO.setSesso(sesso);
		impiegatoUpdateDTO.setEmail(email);
		impiegatoUpdateDTO.setDataNascita(dataNascita);
		impiegatoUpdateDTO.setIdAzienda(idAzienda);
		
		when(impiegatoRepository.existsById(impiegatoUpdateDTO.getId())).thenReturn(true);
		when(impiegatoRepository.existsByEmail(impiegatoUpdateDTO.getEmail())).thenReturn(false);
		
		impiegatoService.update(impiegatoUpdateDTO);
		
		ArgumentCaptor<Impiegato> argumentCaptor = ArgumentCaptor.forClass(Impiegato.class);
		verify(impiegatoRepository).save(argumentCaptor.capture());
		Impiegato impiegatoCatturato = argumentCaptor.getValue();
		
		assertThat(impiegatoCatturato.getId()).isEqualTo(impiegatoUpdateDTO.getId());
		assertThat(impiegatoCatturato.getNome()).isEqualTo(impiegatoUpdateDTO.getNome());
		assertThat(impiegatoCatturato.getCognome()).isEqualTo(impiegatoUpdateDTO.getCognome());
		assertThat(impiegatoCatturato.getSesso()).isEqualTo(impiegatoUpdateDTO.getSesso());
		assertThat(impiegatoCatturato.getDataNascita()).isEqualTo(impiegatoUpdateDTO.getDataNascita());
		assertThat(impiegatoCatturato.getEmail()).isEqualTo(impiegatoUpdateDTO.getEmail());
		assertThat(impiegatoCatturato.getAzienda().getId()).isEqualTo(impiegatoUpdateDTO.getIdAzienda());
		assertThat(impiegatoCatturato.getAzienda().getNome()).isNull();
		assertThat(impiegatoCatturato.getAzienda().getIndirizzo()).isNull();
		assertThat(impiegatoCatturato.getAzienda().getEmail()).isNull();
		assertThat(impiegatoCatturato.getAzienda().getImpiegati()).isNull();
	}
	
	@Test
	void testUpdateFailureExistById() {
		long id = 1L;
		ImpiegatoUpdateDTO impiegatoUpdateDTO = new ImpiegatoUpdateDTO();
		impiegatoUpdateDTO.setId(id);
		
		when(impiegatoRepository.existsById(impiegatoUpdateDTO.getId())).thenReturn(false);

		assertThatThrownBy(() -> impiegatoService.update(impiegatoUpdateDTO))
				.isInstanceOf(ImpiegatoNonTrovatoException.class)
				.hasMessage("Impiegato con id [%s] non trovato".formatted(impiegatoUpdateDTO.getId()));
		
		verify(impiegatoRepository, never()).existsByEmail(any());
		verify(impiegatoRepository, never()).save(any());
		
	}
	
	@Test
	void testUpdateFailureExistByUsername() {
		long id = 1L;
		String email = FAKER.internet().safeEmailAddress();
		ImpiegatoUpdateDTO impiegatoUpdateDTO = new ImpiegatoUpdateDTO();
		impiegatoUpdateDTO.setId(id);
		impiegatoUpdateDTO.setEmail(email);
		
		when(impiegatoRepository.existsById(impiegatoUpdateDTO.getId())).thenReturn(true);
		when(impiegatoRepository.existsByEmail(impiegatoUpdateDTO.getEmail())).thenReturn(true);

		assertThatThrownBy(() -> impiegatoService.update(impiegatoUpdateDTO))
				.isInstanceOf(EmailDuplicataException.class)
				.hasMessage("Impiegato con email [%s] gia' presente".formatted(impiegatoUpdateDTO.getEmail()));
		
		verify(impiegatoRepository, never()).save(any());
		
	}

	@Test
	void testDelete() {
		long id = 1L;
		
		impiegatoService.delete(id);
		
		verify(impiegatoRepository).deleteById(id);
	}

	@Test
	void testCerca() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		Sesso sesso = Sesso.MASCHIO;
		String email = FAKER.internet().safeEmailAddress();
		LocalDate dataNascita = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dataNascitaDa = dataNascita.minusDays(1);
		LocalDate dataNascitaA = dataNascita.plusDays(1);
		ImpiegatoFiltroDTO impiegatoFiltroDTO = new ImpiegatoFiltroDTO();
		impiegatoFiltroDTO.setNome(nome);
		impiegatoFiltroDTO.setCognome(cognome);
		impiegatoFiltroDTO.setSesso(sesso);
		impiegatoFiltroDTO.setEmail(email);
		impiegatoFiltroDTO.setDataNascitaDa(dataNascitaDa);
		impiegatoFiltroDTO.setDataNascitaA(dataNascitaA);
		impiegatoFiltroDTO.setOrdinamento("nome");
		impiegatoFiltroDTO.setDirezione("ASC");
		impiegatoFiltroDTO.setLimite(1);
		impiegatoFiltroDTO.setOffset(0);
		
		List<Impiegato> impiegati = new ArrayList<>();
		long id = 1L;
		long count = 1L;
		long idAzienda = 1L;
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		impiegati.add(
				new Impiegato(
						id, 
						nome, 
						cognome, 
						sesso, 
						dataNascita, 
						email,
						new Azienda(idAzienda, nomeAzienda, indirizzoAzienda, emailAzienda, null)
				)
		);
		
		when(impiegatoJdbcRepository.cerca(impiegatoFiltroDTO)).thenReturn(impiegati);
		when(impiegatoRepository.count()).thenReturn(count);
		
		PaginaDTO<ImpiegatoReadDTO> paginaAttuale = impiegatoService.cerca(impiegatoFiltroDTO);
		
		assertThat(paginaAttuale.getPayload()).hasSize(1);
		assertThat(paginaAttuale.getPayload().get(0).getId()).isEqualTo(id);
		assertThat(paginaAttuale.getPayload().get(0).getNome()).isEqualTo(nome);
		assertThat(paginaAttuale.getPayload().get(0).getCognome()).isEqualTo(cognome);
		assertThat(paginaAttuale.getPayload().get(0).getSesso()).isEqualTo(sesso);
		assertThat(paginaAttuale.getPayload().get(0).getDataNascita()).isBetween(dataNascitaDa, dataNascitaA);
		assertThat(paginaAttuale.getPayload().get(0).getEmail()).isEqualTo(email);
		assertThat(paginaAttuale.getPayload().get(0).getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(paginaAttuale.getPayload().get(0).getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(paginaAttuale.getPayload().get(0).getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(paginaAttuale.getPayload().get(0).getAzienda().getEmail()).isEqualTo(emailAzienda);
	}

}
