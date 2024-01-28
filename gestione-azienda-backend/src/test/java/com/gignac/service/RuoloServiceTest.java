package com.gignac.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gignac.dto.RuoloReadDTO;
import com.gignac.entity.Ruolo;
import com.gignac.exception.RuoloNonTrovatoException;
import com.gignac.mapper.DaRuoloARuoloReadDTOMapper;
import com.gignac.repository.jpa.RuoloRepository;
import com.gignac.service.impl.RuoloServiceImpl;

@ExtendWith(MockitoExtension.class)
class RuoloServiceTest {

	private RuoloService ruoloService;
	
	private DaRuoloARuoloReadDTOMapper daRuoloARuoloReadDTOMapper;

	@Mock
	private RuoloRepository ruoloRepository;

	@BeforeEach
	void setUp() throws Exception {
		daRuoloARuoloReadDTOMapper = new DaRuoloARuoloReadDTOMapper();
		ruoloService = new RuoloServiceImpl(ruoloRepository, daRuoloARuoloReadDTOMapper);
	}

	@Test
	void testReadLong() {
		long id = 1L;
		String codice = "codice";
		String descrizione = "descrizione";
		Ruolo ruolo = new Ruolo(
				id, 
				codice, 
				descrizione, 
				null
		);
		
		when(ruoloRepository.findById(id)).thenReturn(Optional.of(ruolo));
		
		RuoloReadDTO ruoloAttuale = ruoloService.read(id);
		
		assertThat(ruoloAttuale.getId()).isEqualTo(id);
		assertThat(ruoloAttuale.getCodice()).isEqualTo(codice);
		assertThat(ruoloAttuale.getDescrizione()).isEqualTo(descrizione);
	}
	
	@Test
	void testReadLongFailure() {
		Long id = 1L;

		when(ruoloRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> ruoloService.read(id))
				.isInstanceOf(RuoloNonTrovatoException.class)
				.hasMessage("Ruolo con id [%s] non trovato".formatted(id));
	}

	@Test
	void testRead() {
		List<Ruolo> ruoli = new ArrayList<>();
		long id = 1L;
		String codice = "codice";
		String descrizione = "descrizione";
		ruoli.add(
				new Ruolo(
						id, 
						codice, 
						descrizione, 
						null
				)
		);
		
		when(ruoloRepository.findAll()).thenReturn(ruoli);
		
		List<RuoloReadDTO> ruoliAttuali = ruoloService.read();
		
		assertThat(ruoliAttuali).hasSize(1);
		assertThat(ruoliAttuali.get(0).getId()).isEqualTo(id);
		assertThat(ruoliAttuali.get(0).getCodice()).isEqualTo(codice);
		assertThat(ruoliAttuali.get(0).getDescrizione()).isEqualTo(descrizione);
	}

}
