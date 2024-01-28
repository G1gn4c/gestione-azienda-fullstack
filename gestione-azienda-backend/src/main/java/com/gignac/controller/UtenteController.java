package com.gignac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gignac.dto.PaginaDTO;
import com.gignac.dto.UtenteCreateDTO;
import com.gignac.dto.UtenteFiltroDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.dto.UtenteUpdateDTO;
import com.gignac.service.UtenteService;

@RestController
@RequestMapping("/api/v1/utente")
public class UtenteController {
	
	private UtenteService utenteService;
	
	public UtenteController(UtenteService utenteService) {
		super();
		this.utenteService = utenteService;
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody UtenteCreateDTO utenteCreateDTO) {
		utenteService.create(utenteCreateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UtenteReadDTO> read(@PathVariable Long id) {
		UtenteReadDTO utente = utenteService.read(id);
		return new ResponseEntity<UtenteReadDTO>(utente, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<UtenteReadDTO>> read() {
		List<UtenteReadDTO> utenti = utenteService.read();
		return new ResponseEntity<List<UtenteReadDTO>>(utenti, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody UtenteUpdateDTO utenteUpdateDTO) {
		utenteService.update(utenteUpdateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		utenteService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/cerca")
	public ResponseEntity<PaginaDTO<UtenteReadDTO>> cerca(@RequestBody UtenteFiltroDTO utenteFiltroDTO) {
		PaginaDTO<UtenteReadDTO> utentiPaginati = utenteService.cerca(utenteFiltroDTO);
		return new ResponseEntity<PaginaDTO<UtenteReadDTO>>(utentiPaginati, HttpStatus.OK);
	}

}
