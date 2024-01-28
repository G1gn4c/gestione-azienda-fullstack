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

import com.gignac.dto.AziendaCreateDTO;
import com.gignac.dto.AziendaFiltroDTO;
import com.gignac.dto.AziendaReadDTO;
import com.gignac.dto.AziendaUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.service.AziendaService;

@RestController
@RequestMapping("/api/v1/azienda")
public class AziendaController {
	
	private AziendaService aziendaService;

	public AziendaController(AziendaService aziendaService) {
		super();
		this.aziendaService = aziendaService;
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody AziendaCreateDTO aziendaCreateDTO) {
		aziendaService.create(aziendaCreateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AziendaReadDTO> read(@PathVariable Long id) {
		AziendaReadDTO azienda = aziendaService.read(id);
		return new ResponseEntity<AziendaReadDTO>(azienda, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<AziendaReadDTO>> read() {
		List<AziendaReadDTO> aziende = aziendaService.read();
		return new ResponseEntity<List<AziendaReadDTO>>(aziende, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody AziendaUpdateDTO aziendaUpdateDTO) {
		aziendaService.update(aziendaUpdateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		aziendaService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/cerca")
	public ResponseEntity<PaginaDTO<AziendaReadDTO>> cerca(@RequestBody AziendaFiltroDTO aziendaFiltroDTO) {
		PaginaDTO<AziendaReadDTO> aziendePaginati = aziendaService.cerca(aziendaFiltroDTO);
		return new ResponseEntity<PaginaDTO<AziendaReadDTO>>(aziendePaginati, HttpStatus.OK);
	}

}
