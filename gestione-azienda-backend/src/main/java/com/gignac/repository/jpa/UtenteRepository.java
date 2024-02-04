package com.gignac.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gignac.entity.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

	public boolean existsByUsername(String username);
	
	public Optional<Utente> findByUsername(String username);
	
}
