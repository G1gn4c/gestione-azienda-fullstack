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

import com.gignac.dto.ImpiegatoCreateDTO;
import com.gignac.dto.ImpiegatoFiltroDTO;
import com.gignac.dto.ImpiegatoReadDTO;
import com.gignac.dto.ImpiegatoUpdateDTO;
import com.gignac.dto.PaginaDTO;
import com.gignac.service.ImpiegatoService;

@RestController
@RequestMapping("/api/v1/impiegato")
public class ImpiegatoController {
	
	private ImpiegatoService impiegatoService;
	
	public ImpiegatoController(ImpiegatoService impiegatoService) {
		super();
		this.impiegatoService = impiegatoService;
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody ImpiegatoCreateDTO impiegatoCreateDTO) {
		impiegatoService.create(impiegatoCreateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ImpiegatoReadDTO> read(@PathVariable Long id) {
		ImpiegatoReadDTO impiegato = impiegatoService.read(id);
		return new ResponseEntity<ImpiegatoReadDTO>(impiegato, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<ImpiegatoReadDTO>> read() {
		List<ImpiegatoReadDTO> impiegati = impiegatoService.read();
		return new ResponseEntity<List<ImpiegatoReadDTO>>(impiegati, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody ImpiegatoUpdateDTO impiegatoUpdateDTO) {
		impiegatoService.update(impiegatoUpdateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		impiegatoService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/cerca")
	public ResponseEntity<PaginaDTO<ImpiegatoReadDTO>> cerca(@RequestBody ImpiegatoFiltroDTO impiegatoFiltroDTO) {
		PaginaDTO<ImpiegatoReadDTO> impiegatiPaginati = impiegatoService.cerca(impiegatoFiltroDTO);
		return new ResponseEntity<PaginaDTO<ImpiegatoReadDTO>>(impiegatiPaginati, HttpStatus.OK);
	}

}
