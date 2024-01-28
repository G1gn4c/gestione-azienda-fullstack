package com.gignac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gignac.dto.RuoloReadDTO;
import com.gignac.service.RuoloService;

@RestController
@RequestMapping("/api/v1/ruolo")
public class RuoloController {
	
	private RuoloService ruoloService;
	
	public RuoloController(RuoloService ruoloService) {
		super();
		this.ruoloService = ruoloService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<RuoloReadDTO> read(@PathVariable Long id) {
		RuoloReadDTO ruolo = ruoloService.read(id);
		return new ResponseEntity<RuoloReadDTO>(ruolo, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<RuoloReadDTO>> read() {
		List<RuoloReadDTO> ruoli = ruoloService.read();
		return new ResponseEntity<List<RuoloReadDTO>>(ruoli, HttpStatus.OK);
	}
	
}
