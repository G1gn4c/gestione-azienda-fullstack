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
		name = "ruolo",
		schema = "gestione-azienda",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {
						"codice"
				},
				name = "ruolo_codice_unique"
		)
)
public class Ruolo {
	
	@Id
	@SequenceGenerator(
			name = "ruolo_id_sequence",
			schema = "gestione-azienda",
			sequenceName = "ruolo_id_seq",
			allocationSize = 1
	)
	@GeneratedValue(
			generator = "ruolo_id_sequence",
			strategy = GenerationType.SEQUENCE
	)
	private Long id;
	
	@Column(
			nullable = false
	)
	private String codice;
	
	@Column(
			nullable = false
	)
	private String descrizione;
	
	@OneToMany(mappedBy = "ruolo")
	private List<Utente> utenti;

	public Ruolo() {
		super();
	}

	public Ruolo(Long id, String codice, String descrizione, List<Utente> utenti) {
		super();
		this.id = id;
		this.codice = codice;
		this.descrizione = descrizione;
		this.utenti = utenti;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Utente> getUtenti() {
		return utenti;
	}

	public void setUtenti(List<Utente> utenti) {
		this.utenti = utenti;
	}

}
