package com.gignac.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gignac.entity.Azienda;

public interface AziendaRepository extends JpaRepository<Azienda, Long> {

	public boolean existsByEmail(String email);

}
