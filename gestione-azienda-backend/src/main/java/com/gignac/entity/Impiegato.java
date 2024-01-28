package com.gignac.entity;

import java.time.LocalDate;

import com.gignac.enumeration.Sesso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "impiegato",
		schema = "gestione-azienda",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {
						"email"
				},
				name = "impiegato_email_unique"
		)
)
public class Impiegato {
	
	@Id
	@SequenceGenerator(
			name = "impiegato_id_sequence",
			schema = "gestione-azienda",
			sequenceName = "impiegato_id_seq",
			allocationSize = 1
	)
	@GeneratedValue(
			generator = "impiegato_id_sequence",
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
	private String cognome;
	
	@Column(
			nullable = false
	)
	@Enumerated(EnumType.STRING)
	private Sesso sesso;
	
	@Column(
			nullable = false
	)
	private LocalDate dataNascita;
	
	@Column(
			nullable = false
	)
	private String email;
	
	@ManyToOne
	@JoinColumn(
			foreignKey = @ForeignKey(
					name = "impiegato_azienda_fk"
			),
			name = "id_azienda", 
			nullable = false
	)
	private Azienda azienda;
	
	public Impiegato() {
		super();
	}

	public Impiegato(Long id, String nome, String cognome, Sesso sesso, LocalDate dataNascita, String email,
			Azienda azienda) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.sesso = sesso;
		this.dataNascita = dataNascita;
		this.email = email;
		this.azienda = azienda;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Sesso getSesso() {
		return sesso;
	}

	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Azienda getAzienda() {
		return azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}
	
}
