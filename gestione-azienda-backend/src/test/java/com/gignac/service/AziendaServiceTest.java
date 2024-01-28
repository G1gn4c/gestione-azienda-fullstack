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

import com.gignac.dto.AziendaCreateDTO;
import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.dto.AziendaReadDTO;
import com.gignac.dto.AziendaUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.entity.Azienda;
import com.gignac.exception.AziendaNonTrovataException;
import com.gignac.exception.EmailDuplicataException;
import com.gignac.mapper.DaAziendaAdAziendaReadDTOMapper;
import com.gignac.repository.jdbc.AziendaJdbcRepository;
import com.gignac.repository.jpa.AziendaRepository;
import com.gignac.service.impl.AziendaServiceImpl;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class AziendaServiceTest {
	
	private static final Faker FAKER = new Faker();
	
	private AziendaService aziendaService;
	
	private DaAziendaAdAziendaReadDTOMapper daAziendaAdAziendaReadDTOMapper;
	
	@Mock
	private AziendaRepository aziendaRepository;
	
	@Mock
	private AziendaJdbcRepository aziendaJdbcRepository;

	@BeforeEach
	void setUp() throws Exception {
		daAziendaAdAziendaReadDTOMapper = new DaAziendaAdAziendaReadDTOMapper();
		aziendaService = new AziendaServiceImpl(aziendaRepository, aziendaJdbcRepository, daAziendaAdAziendaReadDTOMapper);
	}

	@Test
	void testCreate() {
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nome);
		aziendaCreateDTO.setIndirizzo(indirizzo);
		aziendaCreateDTO.setEmail(email);
		
		when(aziendaRepository.existsByEmail(email)).thenReturn(false);
		
		aziendaService.create(aziendaCreateDTO);
		
		ArgumentCaptor<Azienda> argumentCaptor = ArgumentCaptor.forClass(Azienda.class);
		verify(aziendaRepository).save(argumentCaptor.capture());
		Azienda aziendaCatturata = argumentCaptor.getValue();
		
		assertThat(aziendaCatturata.getId()).isNull();
		assertThat(aziendaCatturata.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaCatturata.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaCatturata.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		assertThat(aziendaCatturata.getImpiegati()).isNull();
	}
	
	@Test
	void testCreateFailure() {
		String email = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setEmail(email);
		
		when(aziendaRepository.existsByEmail(email)).thenReturn(true);
		
		assertThatThrownBy(() -> aziendaService.create(aziendaCreateDTO))
				.isInstanceOf(EmailDuplicataException.class)
				.hasMessage("Azienda con email [%s] gia' presente".formatted(aziendaCreateDTO.getEmail()));
		
		verify(aziendaRepository, never()).save(any());
	}

	@Test
	void testReadLong() {
		long id = 1L;
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		Azienda azienda = new Azienda(
				id, 
				nome, 
				indirizzo, 
				email, 
				new ArrayList<>()
		);
		
		when(aziendaRepository.findById(id)).thenReturn(Optional.of(azienda));
		
		AziendaReadDTO aziendaAttuale = aziendaService.read(id);
		
		assertThat(aziendaAttuale.getId()).isEqualTo(azienda.getId());
		assertThat(aziendaAttuale.getNome()).isEqualTo(azienda.getNome());
		assertThat(aziendaAttuale.getIndirizzo()).isEqualTo(azienda.getIndirizzo());
		assertThat(aziendaAttuale.getEmail()).isEqualTo(azienda.getEmail());
	}
	
	@Test
	void testReadLongFailure() {
		long id = 1L;
		
		when(aziendaRepository.findById(id)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> aziendaService.read(id))
				.isInstanceOf(AziendaNonTrovataException.class)
				.hasMessage("Azienda con id [%s] non trovata".formatted(id));
	}

	@Test
	void testRead() {
		List<Azienda> aziende = new ArrayList<>();
		long id = 1L;
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		aziende.add(
				new Azienda(
						id, 
						nome, 
						indirizzo,
						email, 
						null
				)
		);
		
		when(aziendaRepository.findAll()).thenReturn(aziende);
		
		List<AziendaReadDTO> aziendeAttuali = aziendaService.read();
		
		assertThat(aziendeAttuali).hasSize(1);
		assertThat(aziendeAttuali.get(0).getId()).isEqualTo(id);
		assertThat(aziendeAttuali.get(0).getNome()).isEqualTo(nome);
		assertThat(aziendeAttuali.get(0).getIndirizzo()).isEqualTo(indirizzo);
		assertThat(aziendeAttuali.get(0).getEmail()).isEqualTo(email);
	}

	@Test
	void testUpdate() {
		long id = 1L;
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaUpdateDTO aziendaUpdateDTO = new AziendaUpdateDTO();
		aziendaUpdateDTO.setId(id);
		aziendaUpdateDTO.setNome(nome);
		aziendaUpdateDTO.setIndirizzo(indirizzo);
		aziendaUpdateDTO.setEmail(email);
		
		when(aziendaRepository.existsById(aziendaUpdateDTO.getId())).thenReturn(true);
		when(aziendaRepository.existsByEmail(aziendaUpdateDTO.getEmail())).thenReturn(false);
		
		aziendaService.update(aziendaUpdateDTO);
		
		ArgumentCaptor<Azienda> argumentCaptor = ArgumentCaptor.forClass(Azienda.class);
		verify(aziendaRepository).save(argumentCaptor.capture());
		Azienda aziendaCatturata = argumentCaptor.getValue();
		
		assertThat(aziendaCatturata.getId()).isEqualTo(aziendaUpdateDTO.getId());
		assertThat(aziendaCatturata.getNome()).isEqualTo(aziendaUpdateDTO.getNome());
		assertThat(aziendaCatturata.getIndirizzo()).isEqualTo(aziendaUpdateDTO.getIndirizzo());
		assertThat(aziendaCatturata.getEmail()).isEqualTo(aziendaUpdateDTO.getEmail());
		assertThat(aziendaCatturata.getImpiegati()).isNull();
	}
	
	@Test
	void testUpdateFailureExistsById() {
		long id = 1L;
		AziendaUpdateDTO aziendaUpdateDTO = new AziendaUpdateDTO();
		aziendaUpdateDTO.setId(id);
		
		when(aziendaRepository.existsById(aziendaUpdateDTO.getId())).thenReturn(false);
		
		assertThatThrownBy(() -> aziendaService.update(aziendaUpdateDTO))
				.isInstanceOf(AziendaNonTrovataException.class)
				.hasMessage("Azienda con id [%s] non trovata".formatted(aziendaUpdateDTO.getId()));
		
		verify(aziendaRepository, never()).existsByEmail(any());
		verify(aziendaRepository, never()).save(any());
	}
	
	@Test
	void testUpdateFailureExistsByEmail() {
		long id = 1L;
		String email = FAKER.internet().safeEmailAddress();
		AziendaUpdateDTO aziendaUpdateDTO = new AziendaUpdateDTO();
		aziendaUpdateDTO.setId(id);
		aziendaUpdateDTO.setEmail(email);
		
		when(aziendaRepository.existsById(aziendaUpdateDTO.getId())).thenReturn(true);
		when(aziendaRepository.existsByEmail(aziendaUpdateDTO.getEmail())).thenReturn(true);
		
		assertThatThrownBy(() -> aziendaService.update(aziendaUpdateDTO))
				.isInstanceOf(EmailDuplicataException.class)
				.hasMessage("Azienda con email [%s] gia' presente".formatted(aziendaUpdateDTO.getEmail()));
		
		verify(aziendaRepository, never()).save(any());
	}

	@Test
	void testDelete() {
		long id = 1L;
		
		aziendaService.delete(id);
		
		verify(aziendaRepository).deleteById(id);
	}
	
	@Test
	void testCerca() {
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaFiltroDTO aziendaFiltroDTO = new AziendaFiltroDTO();
		aziendaFiltroDTO.setNome(nome);
		aziendaFiltroDTO.setIndirizzo(indirizzo);
		aziendaFiltroDTO.setEmail(email);
		aziendaFiltroDTO.setOrdinamento("nome");
		aziendaFiltroDTO.setDirezione("ASC");
		aziendaFiltroDTO.setLimite(1);
		aziendaFiltroDTO.setOffset(0);
		
		List<Azienda> aziende = new ArrayList<>();
		long id = 1L;
		long count = 1L;
		aziende.add(
				new Azienda(
						id, 
						nome, 
						indirizzo,
						email, 
						new ArrayList<>()
				)
		);
		
		when(aziendaJdbcRepository.cerca(aziendaFiltroDTO)).thenReturn(aziende);
		when(aziendaRepository.count()).thenReturn(count);
		
		PaginaDTO<AziendaReadDTO> paginaAttuale = aziendaService.cerca(aziendaFiltroDTO);
		
		assertThat(paginaAttuale.getPayload()).hasSize(1);
		assertThat(paginaAttuale.getPayload().get(0).getId()).isEqualTo(id);
		assertThat(paginaAttuale.getPayload().get(0).getNome()).isEqualTo(nome);
		assertThat(paginaAttuale.getPayload().get(0).getIndirizzo()).isEqualTo(indirizzo);
		assertThat(paginaAttuale.getPayload().get(0).getEmail()).isEqualTo(email);
		assertThat(paginaAttuale.getTotale()).isEqualTo(count);
	}

}
