package com.gignac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gignac.service.JasperService;

@RestController
@RequestMapping("/api/v1/jasper")
public class JasperController {
	
	private JasperService jasperService;

	public JasperController(JasperService jasperService) {
		super();
		this.jasperService = jasperService;
	}
	
	@GetMapping
	public ResponseEntity<String> create() throws Exception {
		jasperService.create();
		return new ResponseEntity<String>("File generato con successo", HttpStatus.OK);
	}
	
}
