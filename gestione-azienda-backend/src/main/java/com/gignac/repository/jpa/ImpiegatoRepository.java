package com.gignac.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gignac.entity.Impiegato;

public interface ImpiegatoRepository extends JpaRepository<Impiegato, Long> {
	
	public boolean existsByEmail(String email);

}
