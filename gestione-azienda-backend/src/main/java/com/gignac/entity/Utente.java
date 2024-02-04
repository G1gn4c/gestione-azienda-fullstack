package com.gignac.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
		name = "utente",
		schema = "gestione-azienda",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {
						"username"
				},
				name = "utente_username_unique"
		)
)
public class Utente implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2208369427985563028L;

	@Id
	@SequenceGenerator(			
			name = "utente_id_sequence",
			schema = "gestione-azienda",
			sequenceName = "utente_id_seq",
			allocationSize = 1
	)
	@GeneratedValue(
			generator = "utente_id_sequence",
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
	private String username;
	
	@Column(
			nullable = false
	)
	private String password;
	
	@ManyToOne
	@JoinColumn(
			foreignKey = @ForeignKey(
					name = "utente_ruolo_fk"
			),
			name = "id_ruolo", 
			nullable = false
	)
	private Ruolo ruolo;
	
	public Utente() {
		super();
	}

	public Utente(Long id, String nome, String cognome, String username, String password, Ruolo ruolo) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.ruolo = ruolo;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> ruoli = new ArrayList<>();
		ruoli.add(new SimpleGrantedAuthority(this.ruolo.getCodice()));
		return ruoli;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
