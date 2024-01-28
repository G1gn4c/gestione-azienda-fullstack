alter table if exists "gestione-azienda".impiegato
  add constraint impiegato_azienda_fk foreign key (id_azienda) 
  references "gestione-azienda".azienda;

alter table if exists "gestione-azienda".utente 
  add constraint utente_ruolo_fk foreign key (id_ruolo) 
  references "gestione-azienda".ruolo;