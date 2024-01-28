package com.gignac.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "azienda",
		schema = "gestione-azienda",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {
						"email"
				},
				name = "azienda_email_unique"
		)
)
public class Azienda {
	
	@Id
	@SequenceGenerator(
			name = "azienda_id_sequence",
			schema = "gestione-azienda",
			sequenceName = "azienda_id_seq",
			allocationSize = 1
	)
	@GeneratedValue(
			generator = "azienda_id_sequence",
			strategy = GenerationType.SEQUENCE
	)
	private Long id;
	
	@Column(
			nullable = false
	)
	private String nome;
	
	@Column(
			nullable = false
	)
	private String indirizzo;
	
	@Column(
			nullable = false
	)
	private String email;
	
	@OneToMany(mappedBy = "azienda")
	private List<Impiegato> impiegati;

	public Azienda() {
		super();
	}

	public Azienda(Long id, String nome, String indirizzo, String email, List<Impiegato> impiegati) {
		super();
		this.id = id;
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.email = email;
		this.impiegati = impiegati;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Impiegato> getImpiegati() {
		return impiegati;
	}

	public void setImpiegati(List<Impiegato> impiegati) {
		this.impiegati = impiegati;
	}
	
}
