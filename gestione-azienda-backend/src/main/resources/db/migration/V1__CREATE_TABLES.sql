create table "gestione-azienda".azienda(
   id bigint not null,
   email varchar (255) not null,
   indirizzo varchar (255) not null,
   nome varchar (255) not null,
   primary key (id),
   constraint azienda_email_unique unique (email)
);

create table "gestione-azienda".impiegato(
   data_nascita date not null,
   id bigint not null,
   id_azienda bigint not null,
   cognome varchar (255) not null,
   email varchar (255) not null,
   nome varchar (255) not null,
   sesso varchar (255) not null check(
      sesso in(
         'MASCHIO',
         'FEMMINA'
      )
   ),
   primary key (id),
   constraint impiegato_email_unique unique (email)
);

create table "gestione-azienda".ruolo(
   id bigint not null,
   codice varchar (255) not null,
   descrizione varchar (255) not null,
   primary key (id),
   constraint ruolo_codice_unique unique (codice)
);

create table "gestione-azienda".utente(
   id bigint not null,
   id_ruolo bigint not null,
   cognome varchar (255) not null,
   nome varchar (255) not null,
   password varchar (255) not null,
   username varchar (255) not null,
   primary key (id),
   constraint utente_username_unique unique (username)
);