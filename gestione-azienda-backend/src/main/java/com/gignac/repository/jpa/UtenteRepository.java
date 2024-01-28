package com.gignac.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gignac.entity.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

	public boolean existsByUsername(String username);
	
}
