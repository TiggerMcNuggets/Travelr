# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_company primary key (id)
);

create table computer (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  introduced                    timestamp,
  discontinued                  timestamp,
  company_id                    bigint,
  constraint pk_computer primary key (id)
);

create table destination (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  crd_latitude                  double not null,
  crd_longitude                 double not null,
  destination_type              varchar(255) not null,
  district                      varchar(255),
  country                       varchar(255),
  constraint pk_destination primary key (id)
);

create table nationality (
  id                            bigint auto_increment not null,
  traveller_id                  bigint,
  nationality                   varchar(255),
  has_passport                  boolean not null default 1,
  constraint pk_nationality primary key (id)
);

create table traveller (
  id                            bigint auto_increment not null,
  fname                         varchar(255) not null,
  mname                         varchar(255),
  lname                         varchar(255) not null,
  dob                           timestamp not null,
  gender                        varchar(255) not null,
  email                         varchar(255) not null,
  password                      varchar(255) not null,
  timestamp                     timestamp not null,
  constraint pk_traveller primary key (id)
);

create table traveller_type (
  id                            bigint auto_increment not null,
  t_type                        varchar(255) not null,
  traveller_id                  bigint,
  constraint pk_traveller_type primary key (id)
);

create table trip (
  id                            bigint auto_increment not null,
  traveller_id                  bigint,
  name                          varchar(255),
  constraint pk_trip primary key (id)
);

create table trip_destination (
  id                            bigint auto_increment not null,
  trip_id                       bigint,
  destination_id                bigint,
  arrival_date                  timestamp,
  departure_date                timestamp,
  order_no                      integer not null,
  constraint pk_trip_destination primary key (id)
);

create index ix_computer_company_id on computer (company_id);
alter table computer add constraint fk_computer_company_id foreign key (company_id) references company (id) on delete restrict on update restrict;

create index ix_nationality_traveller_id on nationality (traveller_id);
alter table nationality add constraint fk_nationality_traveller_id foreign key (traveller_id) references traveller (id) on delete restrict on update restrict;

create index ix_traveller_type_traveller_id on traveller_type (traveller_id);
alter table traveller_type add constraint fk_traveller_type_traveller_id foreign key (traveller_id) references traveller (id) on delete restrict on update restrict;

create index ix_trip_traveller_id on trip (traveller_id);
alter table trip add constraint fk_trip_traveller_id foreign key (traveller_id) references traveller (id) on delete restrict on update restrict;

create index ix_trip_destination_trip_id on trip_destination (trip_id);
alter table trip_destination add constraint fk_trip_destination_trip_id foreign key (trip_id) references trip (id) on delete restrict on update restrict;

create index ix_trip_destination_destination_id on trip_destination (destination_id);
alter table trip_destination add constraint fk_trip_destination_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;


# --- !Downs

alter table computer drop constraint if exists fk_computer_company_id;
drop index if exists ix_computer_company_id;

alter table nationality drop constraint if exists fk_nationality_traveller_id;
drop index if exists ix_nationality_traveller_id;

alter table traveller_type drop constraint if exists fk_traveller_type_traveller_id;
drop index if exists ix_traveller_type_traveller_id;

alter table trip drop constraint if exists fk_trip_traveller_id;
drop index if exists ix_trip_traveller_id;

alter table trip_destination drop constraint if exists fk_trip_destination_trip_id;
drop index if exists ix_trip_destination_trip_id;

alter table trip_destination drop constraint if exists fk_trip_destination_destination_id;
drop index if exists ix_trip_destination_destination_id;

drop table if exists company;

drop table if exists computer;

drop table if exists destination;

drop table if exists nationality;

drop table if exists traveller;

drop table if exists traveller_type;

drop table if exists trip;

drop table if exists trip_destination;

