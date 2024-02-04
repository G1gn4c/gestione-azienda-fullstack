package com.gignac.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.gignac.dto.AuthenticationRequestDTO;
import com.gignac.dto.AuthenticationResponseDTO;
import com.gignac.dto.UtenteReadDTO;
import com.gignac.entity.Utente;
import com.gignac.jwt.JwtUtil;
import com.gignac.mapper.DaUtenteAdUtenteReadDTOMapper;
import com.gignac.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationManager authenticationManager;

	private final DaUtenteAdUtenteReadDTOMapper daUtenteAdUtenteReadDTOMapper;

	private final JwtUtil jwtUtil;

	public AuthenticationServiceImpl(
			AuthenticationManager authenticationManager, 
			DaUtenteAdUtenteReadDTOMapper daUtenteAdUtenteReadDTOMapper,
			JwtUtil jwtUtil
	) {
		super();
		this.authenticationManager = authenticationManager;
		this.daUtenteAdUtenteReadDTOMapper = daUtenteAdUtenteReadDTOMapper;
		this.jwtUtil = jwtUtil;
	}

	public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				authenticationRequestDTO.getUsername(), 
				authenticationRequestDTO.getPassword()
		);
		Authentication authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		Utente utente = (Utente) authenticate.getPrincipal();
		UtenteReadDTO utenteReadDTO = this.daUtenteAdUtenteReadDTOMapper.apply(utente);
		String token = this.jwtUtil.issueToken(
				utenteReadDTO.getUsername(), 
				utenteReadDTO.getRuolo()
				             .getCodice()
		);
		AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
		authenticationResponseDTO.setUtenteReadDTO(utenteReadDTO);
		authenticationResponseDTO.setToken(token);
		return authenticationResponseDTO;
	}

}
