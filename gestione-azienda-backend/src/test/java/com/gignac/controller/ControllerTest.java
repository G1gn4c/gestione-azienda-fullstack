package com.gignac.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.gignac.AbstractTest;
import com.gignac.dto.AuthenticationRequestDTO;
import com.gignac.dto.AuthenticationResponseDTO;
import com.gignac.dto.AziendaCreateDTO;
import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.dto.AziendaReadDTO;
import com.gignac.dto.AziendaUpdateDTO;
import com.gignac.dto.ImpiegatoCreateDTO;
import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.dto.ImpiegatoReadDTO;
import com.gignac.dto.ImpiegatoUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.dto.RuoloReadDTO;
import com.gignac.dto.UtenteCreateDTO;
import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.dto.UtenteUpdateDTO;
import com.gignac.enumeration.Sesso;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ControllerTest extends AbstractTest {
	
	private static final String UTENTE_URI = "/api/v1/utente";
	private static final String RUOLO_URI = "/api/v1/ruolo";
	private static final String AZIENDA_URI = "/api/v1/azienda";
	private static final String IMPIEGATO_URI = "/api/v1/impiegato";
	private static final String AUTH_URI = "/api/v1/auth";
	
	private WebTestClient webTestClient;
	
	@Autowired
	public ControllerTest(WebTestClient webTestClient) {
		super();
		this.webTestClient = webTestClient;
	}
	
	/**
	 * UtenteController
	 */

	@Test
	void testCreateUtente() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		UtenteCreateDTO utenteCreateDTO = new UtenteCreateDTO();
		utenteCreateDTO.setNome(nome);
		utenteCreateDTO.setCognome(cognome);
		utenteCreateDTO.setUsername(username);
		utenteCreateDTO.setPassword(password);
		utenteCreateDTO.setIdRuolo(idRuolo);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(UTENTE_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(utenteCreateDTO), UtenteCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<UtenteReadDTO> utenti = webTestClient.get()
	                                              .uri(UTENTE_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<UtenteReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		UtenteReadDTO utenteReadDTO = utenti.stream()
				                            .filter(utente -> utente.getUsername().equals(username))
				                            .findFirst()
				                            .get();
		
		assertThat(utenti).isNotEmpty();
		assertThat(utenteReadDTO.getId()).isNotNull();
		assertThat(utenteReadDTO.getNome()).isEqualTo(utenteCreateDTO.getNome());
		assertThat(utenteReadDTO.getCognome()).isEqualTo(utenteCreateDTO.getCognome());
		assertThat(utenteReadDTO.getUsername()).isEqualTo(utenteCreateDTO.getUsername());
		assertThat(utenteReadDTO.getRuolo().getId()).isEqualTo(utenteCreateDTO.getIdRuolo());
		assertThat(utenteReadDTO.getRuolo().getCodice()).isNotNull();
		assertThat(utenteReadDTO.getRuolo().getDescrizione()).isNotNull();
		
		long id = utenteReadDTO.getId();
		
		UtenteReadDTO utenteAttuale = webTestClient.get()
                                                   .uri(UTENTE_URI + "/{id}", id)
                                                   .accept(MediaType.APPLICATION_JSON)
                                                   .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                                                   .exchange()
                                                   .expectStatus()
                                                   .isOk()
                                                   .expectBody(new ParameterizedTypeReference<UtenteReadDTO>() {})
                                                   .returnResult()
                                                   .getResponseBody();
		
		assertThat(utenteAttuale.getId()).isEqualTo(id);
		assertThat(utenteAttuale.getNome()).isEqualTo(utenteCreateDTO.getNome());
		assertThat(utenteAttuale.getCognome()).isEqualTo(utenteCreateDTO.getCognome());
		assertThat(utenteAttuale.getUsername()).isEqualTo(utenteCreateDTO.getUsername());
		assertThat(utenteAttuale.getRuolo().getId()).isEqualTo(utenteCreateDTO.getIdRuolo());
		assertThat(utenteAttuale.getRuolo().getCodice()).isNotNull();
		assertThat(utenteAttuale.getRuolo().getDescrizione()).isNotNull();
	}

	@Test
	void testUpdateUtente() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		UtenteCreateDTO utenteCreateDTO = new UtenteCreateDTO();
		utenteCreateDTO.setNome(nome);
		utenteCreateDTO.setCognome(cognome);
		utenteCreateDTO.setUsername(username);
		utenteCreateDTO.setPassword(password);
		utenteCreateDTO.setIdRuolo(idRuolo);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(UTENTE_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(utenteCreateDTO), UtenteCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<UtenteReadDTO> utenti = webTestClient.get()
	                                              .uri(UTENTE_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<UtenteReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		UtenteReadDTO utenteReadDTO = utenti.stream()
				                            .filter(utente -> utente.getUsername().equals(username))
				                            .findFirst()
				                            .get();

		assertThat(utenti).isNotEmpty();
		assertThat(utenteReadDTO.getId()).isNotNull();
		assertThat(utenteReadDTO.getNome()).isEqualTo(utenteCreateDTO.getNome());
		assertThat(utenteReadDTO.getCognome()).isEqualTo(utenteCreateDTO.getCognome());
		assertThat(utenteReadDTO.getUsername()).isEqualTo(utenteCreateDTO.getUsername());
		assertThat(utenteReadDTO.getRuolo().getId()).isEqualTo(utenteCreateDTO.getIdRuolo());
		assertThat(utenteReadDTO.getRuolo().getCodice()).isNotNull();
		assertThat(utenteReadDTO.getRuolo().getDescrizione()).isNotNull();
		
		long id = utenteReadDTO.getId();
		String nomeUpdate = FAKER.name().firstName();
		String cognomeUpdate = FAKER.name().lastName();
		String usernameUpdate = FAKER.name().username();
		String passwordUpdate = "passwordUpdate";
		long idRuoloUpdate = 2L;
		UtenteUpdateDTO utenteUpdateDTO = new UtenteUpdateDTO();
		utenteUpdateDTO.setId(id);
		utenteUpdateDTO.setNome(nomeUpdate);
		utenteUpdateDTO.setCognome(cognomeUpdate);
		utenteUpdateDTO.setUsername(usernameUpdate);
		utenteUpdateDTO.setPassword(passwordUpdate);
		utenteUpdateDTO.setIdRuolo(idRuoloUpdate);
		
		webTestClient.put()
                     .uri(UTENTE_URI)
                     .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .body(Mono.just(utenteUpdateDTO), UtenteUpdateDTO.class)
                     .exchange()
                     .expectStatus()
                     .isOk();
		
		utenteReadDTO = webTestClient.get()
				                     .uri(UTENTE_URI + "/{id}", id)
				                     .accept(MediaType.APPLICATION_JSON)
				                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
				                     .exchange()
				                     .expectStatus()
				                     .isOk()
				                     .expectBody(new ParameterizedTypeReference<UtenteReadDTO>() {})
				                     .returnResult()
				                     .getResponseBody();

		assertThat(utenteReadDTO.getId()).isEqualTo(id);
		assertThat(utenteReadDTO.getNome()).isEqualTo(nomeUpdate);
		assertThat(utenteReadDTO.getCognome()).isEqualTo(cognomeUpdate);
		assertThat(utenteReadDTO.getUsername()).isEqualTo(usernameUpdate);
		assertThat(utenteReadDTO.getRuolo().getId()).isEqualTo(idRuoloUpdate);
		assertThat(utenteReadDTO.getRuolo().getCodice()).isNotNull();
		assertThat(utenteReadDTO.getRuolo().getDescrizione()).isNotNull();
	}

	@Test
	void testDeleteUtente() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		UtenteCreateDTO utenteCreateDTO = new UtenteCreateDTO();
		utenteCreateDTO.setNome(nome);
		utenteCreateDTO.setCognome(cognome);
		utenteCreateDTO.setUsername(username);
		utenteCreateDTO.setPassword(password);
		utenteCreateDTO.setIdRuolo(idRuolo);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(UTENTE_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(utenteCreateDTO), UtenteCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<UtenteReadDTO> utenti = webTestClient.get()
	                                              .uri(UTENTE_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<UtenteReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		UtenteReadDTO utenteReadDTO = utenti.stream()
				                            .filter(utente -> utente.getUsername().equals(username))
				                            .findFirst()
				                            .get();

		assertThat(utenti).isNotEmpty();
		assertThat(utenteReadDTO.getId()).isNotNull();
		assertThat(utenteReadDTO.getNome()).isEqualTo(utenteCreateDTO.getNome());
		assertThat(utenteReadDTO.getCognome()).isEqualTo(utenteCreateDTO.getCognome());
		assertThat(utenteReadDTO.getUsername()).isEqualTo(utenteCreateDTO.getUsername());
		assertThat(utenteReadDTO.getRuolo().getId()).isEqualTo(utenteCreateDTO.getIdRuolo());
		assertThat(utenteReadDTO.getRuolo().getCodice()).isNotNull();
		assertThat(utenteReadDTO.getRuolo().getDescrizione()).isNotNull();

		long id = utenteReadDTO.getId();
		
		webTestClient.delete()
                     .uri(UTENTE_URI + "/{id}", id)
                     .accept(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .exchange()
                     .expectStatus()
                     .isOk();
		
		webTestClient.get()
                     .uri(UTENTE_URI + "/{id}", id)
                     .accept(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .exchange()
                     .expectStatus()
                     .isNotFound();
	}

	@Test
	void testCercaUtente() {
		String nome = FAKER.name().firstName();
		String cognome = FAKER.name().lastName();
		String username = FAKER.name().username();
		String password = "password";
		long idRuolo = 1L;
		UtenteCreateDTO utenteCreateDTO = new UtenteCreateDTO();
		utenteCreateDTO.setNome(nome);
		utenteCreateDTO.setCognome(cognome);
		utenteCreateDTO.setUsername(username);
		utenteCreateDTO.setPassword(password);
		utenteCreateDTO.setIdRuolo(idRuolo);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(UTENTE_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(utenteCreateDTO), UtenteCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		UtenteFiltroDTO utenteFiltroDTO = new UtenteFiltroDTO();
		utenteFiltroDTO.setNome(nome);
		utenteFiltroDTO.setCognome(cognome);
		utenteFiltroDTO.setUsername(username);
		utenteFiltroDTO.setOrdinamento("nome");
		utenteFiltroDTO.setDirezione("ASC");
		utenteFiltroDTO.setLimite(1);
		utenteFiltroDTO.setOffset(0);
		
		PaginaDTO<UtenteReadDTO> utenti = webTestClient.post()
	                                                   .uri(UTENTE_URI + "/cerca")
	                                                   .accept(MediaType.APPLICATION_JSON)
	                                                   .contentType(MediaType.APPLICATION_JSON)
	                                                   .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                         		                   .body(Mono.just(utenteFiltroDTO), UtenteFiltroDTO.class)
	                                                   .exchange()
		                                               .expectStatus()
		                                               .isOk()
		                                               .expectBody(new ParameterizedTypeReference<PaginaDTO<UtenteReadDTO>>() {})
		                                               .returnResult()
		                                               .getResponseBody();
		
		assertThat(utenti.getTotale()).isGreaterThan(0);
		assertThat(utenti.getPayload()).hasSize(1);
		assertThat(utenti.getPayload().get(0).getId()).isNotNull();
		assertThat(utenti.getPayload().get(0).getNome()).isEqualTo(nome);
		assertThat(utenti.getPayload().get(0).getCognome()).isEqualTo(cognome);
		assertThat(utenti.getPayload().get(0).getUsername()).isEqualTo(username);
	}
	
	/**
	 * RuoloController
	 */
	
	@Test
	void testReadLong() {
		long id = 1L;
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		RuoloReadDTO ruoloAttuale = webTestClient.get()
                                                 .uri(RUOLO_URI + "/{id}", id)
                                                 .accept(MediaType.APPLICATION_JSON)
                                                 .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                                                 .exchange()
                                                 .expectStatus()
                                                 .isOk()
                                                 .expectBody(new ParameterizedTypeReference<RuoloReadDTO>() {})
                                                 .returnResult()
                                                 .getResponseBody();
		
		assertThat(ruoloAttuale.getId()).isEqualTo(id);
		assertThat(ruoloAttuale.getCodice()).isEqualTo("ROLE_USER");
		assertThat(ruoloAttuale.getDescrizione()).isEqualTo("Ruolo User");
	}

	@Test
	void testRead() {
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		List<RuoloReadDTO> ruoli = webTestClient.get()
                                                .uri(RUOLO_URI)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                                                .exchange()
                                                .expectStatus()
                                                .isOk()
                                                .expectBodyList(new ParameterizedTypeReference<RuoloReadDTO>() {})
                                                .returnResult()
                                                .getResponseBody();
		
		assertThat(ruoli).hasSize(3);
		assertThat(ruoli).anyMatch(ruolo -> {
			return ruolo.getId().equals(1L) && 
				   ruolo.getCodice().equals("ROLE_USER") && 
				   ruolo.getDescrizione().equals("Ruolo User");
		});
		assertThat(ruoli).anyMatch(ruolo -> {
			return ruolo.getId().equals(2L) && 
				   ruolo.getCodice().equals("ROLE_ADMIN") && 
				   ruolo.getDescrizione().equals("Ruolo Admin");
		});
		assertThat(ruoli).anyMatch(ruolo -> {
			return ruolo.getId().equals(3L) && 
				   ruolo.getCodice().equals("ROLE_SUPERADMIN") && 
				   ruolo.getDescrizione().equals("Ruolo Superadmin");
		});
	}
	
	/**
	 * AziendaController
	 */
	
	@Test
	void testCreateAzienda() {
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nome);
		aziendaCreateDTO.setIndirizzo(indirizzo);
		aziendaCreateDTO.setEmail(email);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                              .uri(AZIENDA_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(email))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long id = aziendaReadDTO.getId();
		
		AziendaReadDTO aziendaAttuale = webTestClient.get()
                                                     .uri(AZIENDA_URI + "/{id}", id)
                                                     .accept(MediaType.APPLICATION_JSON)
                                                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                                                     .exchange()
                                                     .expectStatus()
                                                     .isOk()
                                                     .expectBody(new ParameterizedTypeReference<AziendaReadDTO>() {})
                                                     .returnResult()
                                                     .getResponseBody();
		
		assertThat(aziendaAttuale.getId()).isEqualTo(id);
		assertThat(aziendaAttuale.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaAttuale.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaAttuale.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
	}

	@Test
	void testUpdateAzienda() {
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nome);
		aziendaCreateDTO.setIndirizzo(indirizzo);
		aziendaCreateDTO.setEmail(email);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                                .uri(AZIENDA_URI)
	                                                .accept(MediaType.APPLICATION_JSON)
	                                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                                .exchange()
		                                            .expectStatus()
		                                            .isOk()
		                                            .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                            .returnResult()
		                                            .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(email))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long id = aziendaReadDTO.getId();
		String nomeUpdate = FAKER.company().name();
		String indirizzoUpdate = FAKER.address().streetAddressNumber();
		String emailUpdate = FAKER.internet().safeEmailAddress();
		AziendaUpdateDTO aziendaUpdateDTO = new AziendaUpdateDTO();
		aziendaUpdateDTO.setId(id);
		aziendaUpdateDTO.setNome(nomeUpdate);
		aziendaUpdateDTO.setIndirizzo(indirizzoUpdate);
		aziendaUpdateDTO.setEmail(emailUpdate);
		
		webTestClient.put()
                     .uri(AZIENDA_URI)
                     .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .body(Mono.just(aziendaUpdateDTO), AziendaUpdateDTO.class)
                     .exchange()
                     .expectStatus()
                     .isOk();
		
		aziendaReadDTO = webTestClient.get()
				                      .uri(AZIENDA_URI + "/{id}", id)
				                      .accept(MediaType.APPLICATION_JSON)
				                      .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
				                      .exchange()
				                      .expectStatus()
				                      .isOk()
				                      .expectBody(new ParameterizedTypeReference<AziendaReadDTO>() {})
				                      .returnResult()
				                      .getResponseBody();

		assertThat(aziendaReadDTO.getId()).isEqualTo(id);
		assertThat(aziendaReadDTO.getNome()).isEqualTo(nomeUpdate);
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(indirizzoUpdate);
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(emailUpdate);
	}

	@Test
	void testDeleteAzienda() {
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nome);
		aziendaCreateDTO.setIndirizzo(indirizzo);
		aziendaCreateDTO.setEmail(email);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                                .uri(AZIENDA_URI)
	                                                .accept(MediaType.APPLICATION_JSON)
	                                                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                                .exchange()
		                                            .expectStatus()
		                                            .isOk()
		                                            .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                            .returnResult()
		                                            .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(email))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long id = aziendaReadDTO.getId();
		
		webTestClient.delete()
                     .uri(AZIENDA_URI + "/{id}", id)
                     .accept(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .exchange()
                     .expectStatus()
                     .isOk();
		
		webTestClient.get()
                     .uri(AZIENDA_URI + "/{id}", id)
                     .accept(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .exchange()
                     .expectStatus()
                     .isNotFound();
	}

	@Test
	void testCercaAzienda() {
		String nome = FAKER.company().name();
		String indirizzo = FAKER.address().streetAddressNumber();
		String email = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nome);
		aziendaCreateDTO.setIndirizzo(indirizzo);
		aziendaCreateDTO.setEmail(email);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		AziendaFiltroDTO aziendaFiltroDTO = new AziendaFiltroDTO();
		aziendaFiltroDTO.setNome(nome);
		aziendaFiltroDTO.setIndirizzo(indirizzo);
		aziendaFiltroDTO.setEmail(email);
		aziendaFiltroDTO.setOrdinamento("nome");
		aziendaFiltroDTO.setDirezione("ASC");
		aziendaFiltroDTO.setLimite(1);
		aziendaFiltroDTO.setOffset(0);
		
		PaginaDTO<AziendaReadDTO> aziende = webTestClient.post()
	                                                     .uri(AZIENDA_URI + "/cerca")
	                                                     .accept(MediaType.APPLICATION_JSON)
	                                                     .contentType(MediaType.APPLICATION_JSON)
	                                                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                         		                     .body(Mono.just(aziendaFiltroDTO), AziendaFiltroDTO.class)
	                                                     .exchange()
		                                                 .expectStatus()
		                                                 .isOk()
		                                                 .expectBody(new ParameterizedTypeReference<PaginaDTO<AziendaReadDTO>>() {})
		                                                 .returnResult()
		                                                 .getResponseBody();
		
		assertThat(aziende.getTotale()).isGreaterThan(0);
		assertThat(aziende.getPayload()).hasSize(1);
		assertThat(aziende.getPayload().get(0).getId()).isNotNull();
		assertThat(aziende.getPayload().get(0).getNome()).isEqualTo(nome);
		assertThat(aziende.getPayload().get(0).getIndirizzo()).isEqualTo(indirizzo);
		assertThat(aziende.getPayload().get(0).getEmail()).isEqualTo(email);
	}
	
	/**
	 * ImpiegatoController
	 */
	
	@Test
	void testCreateImpiegato() {
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nomeAzienda);
		aziendaCreateDTO.setIndirizzo(indirizzoAzienda);
		aziendaCreateDTO.setEmail(emailAzienda);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                              .uri(AZIENDA_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(emailAzienda))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long idAzienda = aziendaReadDTO.getId();
		
		String nomeImpiegato = FAKER.name().firstName();
		String cognomeImpiegato = FAKER.name().lastName();
		Sesso sessoImpiegato = Sesso.MASCHIO;
		String emailImpiegato = FAKER.internet().safeEmailAddress();
		LocalDate dataNascitaImpiegato = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ImpiegatoCreateDTO impiegatoCreateDTO = new ImpiegatoCreateDTO();
		impiegatoCreateDTO.setNome(nomeImpiegato);
		impiegatoCreateDTO.setCognome(cognomeImpiegato);
		impiegatoCreateDTO.setSesso(sessoImpiegato);
		impiegatoCreateDTO.setEmail(emailImpiegato);
		impiegatoCreateDTO.setIdAzienda(idAzienda);
		impiegatoCreateDTO.setDataNascita(dataNascitaImpiegato);
		
		webTestClient.post()
		             .uri(IMPIEGATO_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(impiegatoCreateDTO), ImpiegatoCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<ImpiegatoReadDTO> impiegati = webTestClient.get()
	                                                    .uri(IMPIEGATO_URI)
	                                                    .accept(MediaType.APPLICATION_JSON)
	                                                    .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                                    .exchange()
		                                                .expectStatus()
		                                                .isOk()
		                                                .expectBodyList(new ParameterizedTypeReference<ImpiegatoReadDTO>() {})
		                                                .returnResult()
		                                                .getResponseBody();
		
		ImpiegatoReadDTO impiegatoReadDTO = impiegati.stream()
				                                     .filter(impiegato -> impiegato.getEmail().equals(emailImpiegato))
				                                     .findFirst()
				                                     .get();
		
		assertThat(impiegati).isNotEmpty();
		assertThat(impiegatoReadDTO.getId()).isNotNull();
		assertThat(impiegatoReadDTO.getNome()).isEqualTo(impiegatoCreateDTO.getNome());
		assertThat(impiegatoReadDTO.getCognome()).isEqualTo(impiegatoCreateDTO.getCognome());
		assertThat(impiegatoReadDTO.getSesso()).isEqualTo(impiegatoCreateDTO.getSesso());
		assertThat(impiegatoReadDTO.getDataNascita()).isEqualTo(impiegatoCreateDTO.getDataNascita());
		assertThat(impiegatoReadDTO.getEmail()).isEqualTo(impiegatoCreateDTO.getEmail());
		assertThat(impiegatoReadDTO.getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getEmail()).isEqualTo(emailAzienda);
		
		long idImpiegato = impiegatoReadDTO.getId();
		
		ImpiegatoReadDTO impiegatoAttuale = webTestClient.get()
                                                         .uri(IMPIEGATO_URI + "/{id}", idImpiegato)
                                                         .accept(MediaType.APPLICATION_JSON)
                                                         .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                                                         .exchange()
                                                         .expectStatus()
                                                         .isOk()
                                                         .expectBody(new ParameterizedTypeReference<ImpiegatoReadDTO>() {})
                                                         .returnResult()
                                                         .getResponseBody();
		
		assertThat(impiegatoAttuale.getId()).isEqualTo(idImpiegato);
		assertThat(impiegatoAttuale.getNome()).isEqualTo(impiegatoCreateDTO.getNome());
		assertThat(impiegatoAttuale.getCognome()).isEqualTo(impiegatoCreateDTO.getCognome());
		assertThat(impiegatoAttuale.getSesso()).isEqualTo(impiegatoCreateDTO.getSesso());
		assertThat(impiegatoAttuale.getDataNascita()).isEqualTo(impiegatoCreateDTO.getDataNascita());
		assertThat(impiegatoAttuale.getEmail()).isEqualTo(impiegatoCreateDTO.getEmail());
		assertThat(impiegatoAttuale.getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(impiegatoAttuale.getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(impiegatoAttuale.getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(impiegatoAttuale.getAzienda().getEmail()).isEqualTo(emailAzienda);
	}

	@Test
	void testUpdateImpiegato() {
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nomeAzienda);
		aziendaCreateDTO.setIndirizzo(indirizzoAzienda);
		aziendaCreateDTO.setEmail(emailAzienda);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                              .uri(AZIENDA_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(emailAzienda))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long idAzienda = aziendaReadDTO.getId();
		
		String nomeImpiegato = FAKER.name().firstName();
		String cognomeImpiegato = FAKER.name().lastName();
		Sesso sessoImpiegato = Sesso.MASCHIO;
		String emailImpiegato = FAKER.internet().safeEmailAddress();
		LocalDate dataNascitaImpiegato = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ImpiegatoCreateDTO impiegatoCreateDTO = new ImpiegatoCreateDTO();
		impiegatoCreateDTO.setNome(nomeImpiegato);
		impiegatoCreateDTO.setCognome(cognomeImpiegato);
		impiegatoCreateDTO.setSesso(sessoImpiegato);
		impiegatoCreateDTO.setEmail(emailImpiegato);
		impiegatoCreateDTO.setIdAzienda(idAzienda);
		impiegatoCreateDTO.setDataNascita(dataNascitaImpiegato);
		
		webTestClient.post()
		             .uri(IMPIEGATO_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(impiegatoCreateDTO), ImpiegatoCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<ImpiegatoReadDTO> impiegati = webTestClient.get()
	                                                    .uri(IMPIEGATO_URI)
	                                                    .accept(MediaType.APPLICATION_JSON)
	                                                    .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                                    .exchange()
		                                                .expectStatus()
		                                                .isOk()
		                                                .expectBodyList(new ParameterizedTypeReference<ImpiegatoReadDTO>() {})
		                                                .returnResult()
		                                                .getResponseBody();
		
		ImpiegatoReadDTO impiegatoReadDTO = impiegati.stream()
				                                     .filter(impiegato -> impiegato.getEmail().equals(emailImpiegato))
				                                     .findFirst()
				                                     .get();
		
		assertThat(impiegati).isNotEmpty();
		assertThat(impiegatoReadDTO.getId()).isNotNull();
		assertThat(impiegatoReadDTO.getNome()).isEqualTo(impiegatoCreateDTO.getNome());
		assertThat(impiegatoReadDTO.getCognome()).isEqualTo(impiegatoCreateDTO.getCognome());
		assertThat(impiegatoReadDTO.getSesso()).isEqualTo(impiegatoCreateDTO.getSesso());
		assertThat(impiegatoReadDTO.getDataNascita()).isEqualTo(impiegatoCreateDTO.getDataNascita());
		assertThat(impiegatoReadDTO.getEmail()).isEqualTo(impiegatoCreateDTO.getEmail());
		assertThat(impiegatoReadDTO.getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getEmail()).isEqualTo(emailAzienda);
		
		long idImpiegato = impiegatoReadDTO.getId();
		String nomeImpiegatoUpdate = FAKER.name().firstName();
		String cognomeImpiegatoUpdate = FAKER.name().lastName();
		Sesso sessoImpiegatoUpdate = Sesso.FEMMINA;
		String emailImpiegatoUpdate = FAKER.internet().safeEmailAddress();
		ImpiegatoUpdateDTO impiegatoUpdateDTO = new ImpiegatoUpdateDTO();
		impiegatoUpdateDTO.setId(idImpiegato);
		impiegatoUpdateDTO.setNome(nomeImpiegatoUpdate);
		impiegatoUpdateDTO.setCognome(cognomeImpiegatoUpdate);
		impiegatoUpdateDTO.setSesso(sessoImpiegatoUpdate);
		impiegatoUpdateDTO.setDataNascita(dataNascitaImpiegato);
		impiegatoUpdateDTO.setEmail(emailImpiegatoUpdate);
		impiegatoUpdateDTO.setIdAzienda(idAzienda);
		
		webTestClient.put()
		             .uri(IMPIEGATO_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
				     .body(Mono.just(impiegatoUpdateDTO), ImpiegatoUpdateDTO.class)
				     .exchange()
				     .expectStatus()
				     .isOk();

		impiegatoReadDTO = webTestClient.get()
				                        .uri(IMPIEGATO_URI + "/{id}", idImpiegato)
				                        .accept(MediaType.APPLICATION_JSON)
				                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
				                        .exchange()
				                        .expectStatus()
				                        .isOk()
				                        .expectBody(new ParameterizedTypeReference<ImpiegatoReadDTO>() {})
				                        .returnResult()
				                        .getResponseBody();
		
		assertThat(impiegatoReadDTO.getId()).isEqualTo(idImpiegato);
		assertThat(impiegatoReadDTO.getNome()).isEqualTo(nomeImpiegatoUpdate);
		assertThat(impiegatoReadDTO.getCognome()).isEqualTo(cognomeImpiegatoUpdate);
		assertThat(impiegatoReadDTO.getSesso()).isEqualTo(sessoImpiegatoUpdate);
		assertThat(impiegatoReadDTO.getDataNascita()).isEqualTo(dataNascitaImpiegato);
		assertThat(impiegatoReadDTO.getEmail()).isEqualTo(emailImpiegatoUpdate);
		assertThat(impiegatoReadDTO.getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getEmail()).isEqualTo(emailAzienda);
	}

	@Test
	void testDeleteImpiegato() {
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nomeAzienda);
		aziendaCreateDTO.setIndirizzo(indirizzoAzienda);
		aziendaCreateDTO.setEmail(emailAzienda);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                              .uri(AZIENDA_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(emailAzienda))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long idAzienda = aziendaReadDTO.getId();
		
		String nomeImpiegato = FAKER.name().firstName();
		String cognomeImpiegato = FAKER.name().lastName();
		Sesso sessoImpiegato = Sesso.MASCHIO;
		String emailImpiegato = FAKER.internet().safeEmailAddress();
		LocalDate dataNascitaImpiegato = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ImpiegatoCreateDTO impiegatoCreateDTO = new ImpiegatoCreateDTO();
		impiegatoCreateDTO.setNome(nomeImpiegato);
		impiegatoCreateDTO.setCognome(cognomeImpiegato);
		impiegatoCreateDTO.setSesso(sessoImpiegato);
		impiegatoCreateDTO.setEmail(emailImpiegato);
		impiegatoCreateDTO.setIdAzienda(idAzienda);
		impiegatoCreateDTO.setDataNascita(dataNascitaImpiegato);
		
		webTestClient.post()
		             .uri(IMPIEGATO_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(impiegatoCreateDTO), ImpiegatoCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<ImpiegatoReadDTO> impiegati = webTestClient.get()
	                                                    .uri(IMPIEGATO_URI)
	                                                    .accept(MediaType.APPLICATION_JSON)
	                                                    .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                                    .exchange()
		                                                .expectStatus()
		                                                .isOk()
		                                                .expectBodyList(new ParameterizedTypeReference<ImpiegatoReadDTO>() {})
		                                                .returnResult()
		                                                .getResponseBody();
		
		ImpiegatoReadDTO impiegatoReadDTO = impiegati.stream()
				                                     .filter(impiegato -> impiegato.getEmail().equals(emailImpiegato))
				                                     .findFirst()
				                                     .get();
		
		assertThat(impiegati).isNotEmpty();
		assertThat(impiegatoReadDTO.getId()).isNotNull();
		assertThat(impiegatoReadDTO.getNome()).isEqualTo(impiegatoCreateDTO.getNome());
		assertThat(impiegatoReadDTO.getCognome()).isEqualTo(impiegatoCreateDTO.getCognome());
		assertThat(impiegatoReadDTO.getSesso()).isEqualTo(impiegatoCreateDTO.getSesso());
		assertThat(impiegatoReadDTO.getDataNascita()).isEqualTo(impiegatoCreateDTO.getDataNascita());
		assertThat(impiegatoReadDTO.getEmail()).isEqualTo(impiegatoCreateDTO.getEmail());
		assertThat(impiegatoReadDTO.getAzienda().getId()).isEqualTo(idAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getNome()).isEqualTo(nomeAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getIndirizzo()).isEqualTo(indirizzoAzienda);
		assertThat(impiegatoReadDTO.getAzienda().getEmail()).isEqualTo(emailAzienda);
		
		long idImpiegato = impiegatoReadDTO.getId();
		
		webTestClient.delete()
                     .uri(IMPIEGATO_URI + "/{id}", idImpiegato)
                     .accept(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .exchange()
                     .expectStatus()
                     .isOk();

        webTestClient.get()
                     .uri(IMPIEGATO_URI + "/{id}", idImpiegato)
                     .accept(MediaType.APPLICATION_JSON)
                     .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                     .exchange()
                     .expectStatus()
                     .isNotFound();
	}

	@Test
	void testCercaImpiegato() {
		String nomeAzienda = FAKER.company().name();
		String indirizzoAzienda = FAKER.address().streetAddressNumber();
		String emailAzienda = FAKER.internet().safeEmailAddress();
		AziendaCreateDTO aziendaCreateDTO = new AziendaCreateDTO();
		aziendaCreateDTO.setNome(nomeAzienda);
		aziendaCreateDTO.setIndirizzo(indirizzoAzienda);
		aziendaCreateDTO.setEmail(emailAzienda);
		
		AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
		authenticationRequestDTO.setUsername("Gignac");
		authenticationRequestDTO.setPassword("password");
		
		String jwtToken = webTestClient.post()
                                       .uri(AUTH_URI + "/login")
                                       .accept(MediaType.APPLICATION_JSON)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .body(Mono.just(authenticationRequestDTO), AuthenticationRequestDTO.class)
                                       .exchange()
                                       .expectStatus()
                                       .isOk()
                                       .expectBody(new ParameterizedTypeReference<AuthenticationResponseDTO>() {})
                                       .returnResult()
                                       .getResponseHeaders()
                                       .get(HttpHeaders.AUTHORIZATION)
                                       .get(0);
		
		webTestClient.post()
		             .uri(AZIENDA_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(aziendaCreateDTO), AziendaCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		List<AziendaReadDTO> aziende = webTestClient.get()
	                                              .uri(AZIENDA_URI)
	                                              .accept(MediaType.APPLICATION_JSON)
	                                              .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                                              .exchange()
		                                          .expectStatus()
		                                          .isOk()
		                                          .expectBodyList(new ParameterizedTypeReference<AziendaReadDTO>() {})
		                                          .returnResult()
		                                          .getResponseBody();
		
		AziendaReadDTO aziendaReadDTO = aziende.stream()
				                               .filter(azienda -> azienda.getEmail().equals(emailAzienda))
				                               .findFirst()
				                               .get();
		
		assertThat(aziende).isNotEmpty();
		assertThat(aziendaReadDTO.getId()).isNotNull();
		assertThat(aziendaReadDTO.getNome()).isEqualTo(aziendaCreateDTO.getNome());
		assertThat(aziendaReadDTO.getIndirizzo()).isEqualTo(aziendaCreateDTO.getIndirizzo());
		assertThat(aziendaReadDTO.getEmail()).isEqualTo(aziendaCreateDTO.getEmail());
		
		long idAzienda = aziendaReadDTO.getId();
		
		String nomeImpiegato = FAKER.name().firstName();
		String cognomeImpiegato = FAKER.name().lastName();
		Sesso sessoImpiegato = Sesso.MASCHIO;
		String emailImpiegato = FAKER.internet().safeEmailAddress();
		LocalDate dataNascitaImpiegato = FAKER.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ImpiegatoCreateDTO impiegatoCreateDTO = new ImpiegatoCreateDTO();
		impiegatoCreateDTO.setNome(nomeImpiegato);
		impiegatoCreateDTO.setCognome(cognomeImpiegato);
		impiegatoCreateDTO.setSesso(sessoImpiegato);
		impiegatoCreateDTO.setEmail(emailImpiegato);
		impiegatoCreateDTO.setIdAzienda(idAzienda);
		impiegatoCreateDTO.setDataNascita(dataNascitaImpiegato);
		
		webTestClient.post()
		             .uri(IMPIEGATO_URI)
		             .accept(MediaType.APPLICATION_JSON)
		             .contentType(MediaType.APPLICATION_JSON)
		             .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
		             .body(Mono.just(impiegatoCreateDTO), ImpiegatoCreateDTO.class)
		             .exchange()
		             .expectStatus()
		             .isOk();
		
		LocalDate dataNascitaDa = dataNascitaImpiegato.minusDays(1);
		LocalDate dataNascitaA = dataNascitaImpiegato.plusDays(1);
		ImpiegatoFiltroDTO impiegatoFiltroDTO = new ImpiegatoFiltroDTO();
		impiegatoFiltroDTO.setNome(nomeImpiegato);
		impiegatoFiltroDTO.setCognome(cognomeImpiegato);
		impiegatoFiltroDTO.setSesso(sessoImpiegato);
		impiegatoFiltroDTO.setEmail(emailImpiegato);
		impiegatoFiltroDTO.setDataNascitaDa(dataNascitaDa);
		impiegatoFiltroDTO.setDataNascitaA(dataNascitaA);
		impiegatoFiltroDTO.setOrdinamento("nome");
		impiegatoFiltroDTO.setDirezione("ASC");
		impiegatoFiltroDTO.setLimite(1);
		impiegatoFiltroDTO.setOffset(0);
		
		PaginaDTO<ImpiegatoReadDTO> impiegati = webTestClient.post()
	                                                         .uri(IMPIEGATO_URI + "/cerca")
	                                                         .accept(MediaType.APPLICATION_JSON)
	                                                         .contentType(MediaType.APPLICATION_JSON)
	                                                         .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
	                         		                         .body(Mono.just(impiegatoFiltroDTO), ImpiegatoFiltroDTO.class)
	                                                         .exchange()
		                                                     .expectStatus()
		                                                     .isOk()
		                                                     .expectBody(new ParameterizedTypeReference<PaginaDTO<ImpiegatoReadDTO>>() {})
		                                                     .returnResult()
		                                                     .getResponseBody();
		
		assertThat(impiegati.getTotale()).isGreaterThan(0);
		assertThat(impiegati.getPayload()).hasSize(1);
		assertThat(impiegati.getPayload().get(0).getId()).isNotNull();
		assertThat(impiegati.getPayload().get(0).getNome()).isEqualTo(nomeImpiegato);
		assertThat(impiegati.getPayload().get(0).getCognome()).isEqualTo(cognomeImpiegato);
		assertThat(impiegati.getPayload().get(0).getSesso()).isEqualTo(sessoImpiegato);
		assertThat(impiegati.getPayload().get(0).getDataNascita()).isEqualTo(dataNascitaImpiegato);
		assertThat(impiegati.getPayload().get(0).getEmail()).isEqualTo(emailImpiegato);
		assertThat(impiegati.getPayload().get(0).getAzienda().getId()).isEqualTo(idAzienda);
	}

}
