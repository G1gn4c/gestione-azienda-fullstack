package com.gignac.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gignac.dto.AuthenticationRequestDTO;
import com.gignac.dto.AuthenticationResponseDTO;
import com.gignac.service.impl.AuthenticationServiceImpl;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationServiceImpl authenticationService;

	public AuthenticationController(AuthenticationServiceImpl authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO authenticationRequest) {
		AuthenticationResponseDTO authenticationResponse = this.authenticationService.login(authenticationRequest);
		return ResponseEntity.ok()
				             .header(HttpHeaders.AUTHORIZATION, authenticationResponse.getToken())
				             .body(authenticationResponse);
	}

}
