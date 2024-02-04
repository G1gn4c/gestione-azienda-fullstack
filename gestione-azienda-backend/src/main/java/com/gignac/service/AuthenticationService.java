package com.gignac.service;

import com.gignac.dto.AuthenticationRequestDTO;
import com.gignac.dto.AuthenticationResponseDTO;

public interface AuthenticationService {

	public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO);

}
