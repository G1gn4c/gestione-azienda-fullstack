package com.gignac.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gignac.repository.jpa.UtenteRepository;

@Service
public class UtenteUserDetailsServiceImpl implements UserDetailsService {
	
	private UtenteRepository utenteRepository;
	
	public UtenteUserDetailsServiceImpl(UtenteRepository utenteRepository) {
		super();
		this.utenteRepository = utenteRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return utenteRepository.findByUsername(username)
				               .orElseThrow(() -> new UsernameNotFoundException("Utente con username [%s] non trovato".formatted(username)));
	}

}
