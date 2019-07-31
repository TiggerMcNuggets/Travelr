-- !Ups

create table destination (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  latitude                      double not null,
  longitude                     double not null,
  type                          varchar(255) not null,
  district                      varchar(255) not null,
  country                       varchar(255) not null,
  user_id                       bigint,
  is_public                     boolean default false not null,
  deleted                       boolean default false not null,
  constraint pk_destination primary key (id)
);

create table destination_traveller_type (
  destination_id                bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_destination_traveller_type primary key (destination_id,traveller_type_id)
);

create table destination_edit_request (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  destination_id                bigint,
  deleted                       boolean default false not null,
  constraint pk_destination_edit_request primary key (id)
);

create table destination_edit_request_traveller_type (
  destination_edit_request_id   bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_destination_edit_request_traveller_type primary key (destination_edit_request_id,traveller_type_id)
);

create table destination_photo (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  destination_id                bigint,
  photo_filename                varchar(255) not null,
  is_public                     boolean default 0 not null,
  deleted                       boolean default false not null,
  constraint pk_destination_photo primary key (id)
);

create table nationality (
  id                            bigint auto_increment not null,
  is_old                        boolean default 0 not null,
  name                          varchar(255),
  deleted                       boolean default false not null,
  constraint pk_nationality primary key (id)
);

create table personal_photo (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  photo_filename                varchar(255) not null,
  is_public                     boolean default 0 not null,
  deleted                       boolean default false not null,
  constraint pk_personal_photo primary key (id)
);

create table traveller_type (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  deleted                       boolean default false not null,
  constraint pk_traveller_type primary key (id)
);

create table trip (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  name                          varchar(255),
  deleted                       boolean default false not null,
  constraint pk_trip primary key (id)
);

create table trip_destination (
  id                            bigint auto_increment not null,
  trip_id                       bigint,
  destination_id                bigint,
  arrival_date                  integer,
  departure_date                integer,
  name                          varchar(255),
  ordinal                       integer not null,
  deleted                       boolean default false not null,
  constraint pk_trip_destination primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  first_name                    varchar(150) not null,
  middle_name                   varchar(64),
  last_name                     varchar(150) not null,
  token                         varchar(255),
  date_of_birth                 integer not null,
  gender                        varchar(255) not null,
  email                         varchar(150) not null,
  password                      varbinary(128),
  user_profile_photo            varchar(255),
  timestamp                     bigint not null,
  account_type                  integer default 0 not null,
  deleted                       boolean default false not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id)
);

create table user_traveller_type (
  user_id                       bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_user_traveller_type primary key (user_id,traveller_type_id)
);

create table user_nationality (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  nationality_id                bigint,
  has_passport                  boolean not null default false not null,
  deleted                       boolean default false not null,
  constraint pk_user_nationality primary key (id)
);

create index ix_destination_user_id on destination (user_id);
alter table destination add constraint fk_destination_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_destination_traveller_type_destination on destination_traveller_type (destination_id);
alter table destination_traveller_type add constraint fk_destination_traveller_type_destination foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_destination_traveller_type_traveller_type on destination_traveller_type (traveller_type_id);
alter table destination_traveller_type add constraint fk_destination_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_destination_edit_request_user_id on destination_edit_request (user_id);
alter table destination_edit_request add constraint fk_destination_edit_request_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_destination_edit_request_destination_id on destination_edit_request (destination_id);
alter table destination_edit_request add constraint fk_destination_edit_request_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_destination_edit_request_traveller_type_destination_ed_1 on destination_edit_request_traveller_type (destination_edit_request_id);
alter table destination_edit_request_traveller_type add constraint fk_destination_edit_request_traveller_type_destination_ed_1 foreign key (destination_edit_request_id) references destination_edit_request (id) on delete restrict on update restrict;

create index ix_destination_edit_request_traveller_type_traveller_type on destination_edit_request_traveller_type (traveller_type_id);
alter table destination_edit_request_traveller_type add constraint fk_destination_edit_request_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_destination_photo_user_id on destination_photo (user_id);
alter table destination_photo add constraint fk_destination_photo_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_destination_photo_destination_id on destination_photo (destination_id);
alter table destination_photo add constraint fk_destination_photo_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_personal_photo_user_id on personal_photo (user_id);
alter table personal_photo add constraint fk_personal_photo_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_trip_user_id on trip (user_id);
alter table trip add constraint fk_trip_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_trip_destination_trip_id on trip_destination (trip_id);
alter table trip_destination add constraint fk_trip_destination_trip_id foreign key (trip_id) references trip (id) on delete restrict on update restrict;

create index ix_trip_destination_destination_id on trip_destination (destination_id);
alter table trip_destination add constraint fk_trip_destination_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_user_traveller_type_user on user_traveller_type (user_id);
alter table user_traveller_type add constraint fk_user_traveller_type_user foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_traveller_type_traveller_type on user_traveller_type (traveller_type_id);
alter table user_traveller_type add constraint fk_user_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_user_nationality_user_id on user_nationality (user_id);
alter table user_nationality add constraint fk_user_nationality_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_nationality_nationality_id on user_nationality (nationality_id);
alter table user_nationality add constraint fk_user_nationality_nationality_id foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

-- !Downs

alter table destination drop constraint if exists fk_destination_user_id;
drop index if exists ix_destination_user_id;

alter table destination_traveller_type drop constraint if exists fk_destination_traveller_type_destination;
drop index if exists ix_destination_traveller_type_destination;

alter table destination_traveller_type drop constraint if exists fk_destination_traveller_type_traveller_type;
drop index if exists ix_destination_traveller_type_traveller_type;

alter table destination_edit_request drop constraint if exists fk_destination_edit_request_user_id;
drop index if exists ix_destination_edit_request_user_id;

alter table destination_edit_request drop constraint if exists fk_destination_edit_request_destination_id;
drop index if exists ix_destination_edit_request_destination_id;

alter table destination_edit_request_traveller_type drop constraint if exists fk_destination_edit_request_traveller_type_destination_ed_1;
drop index if exists ix_destination_edit_request_traveller_type_destination_ed_1;

alter table destination_edit_request_traveller_type drop constraint if exists fk_destination_edit_request_traveller_type_traveller_type;
drop index if exists ix_destination_edit_request_traveller_type_traveller_type;

alter table destination_photo drop constraint if exists fk_destination_photo_user_id;
drop index if exists ix_destination_photo_user_id;

alter table destination_photo drop constraint if exists fk_destination_photo_destination_id;
drop index if exists ix_destination_photo_destination_id;

alter table personal_photo drop constraint if exists fk_personal_photo_user_id;
drop index if exists ix_personal_photo_user_id;

alter table trip drop constraint if exists fk_trip_user_id;
drop index if exists ix_trip_user_id;

alter table trip_destination drop constraint if exists fk_trip_destination_trip_id;
drop index if exists ix_trip_destination_trip_id;

alter table trip_destination drop constraint if exists fk_trip_destination_destination_id;
drop index if exists ix_trip_destination_destination_id;

alter table user_traveller_type drop constraint if exists fk_user_traveller_type_user;
drop index if exists ix_user_traveller_type_user;

alter table user_traveller_type drop constraint if exists fk_user_traveller_type_traveller_type;
drop index if exists ix_user_traveller_type_traveller_type;

alter table user_nationality drop constraint if exists fk_user_nationality_user_id;
drop index if exists ix_user_nationality_user_id;

alter table user_nationality drop constraint if exists fk_user_nationality_nationality_id;
drop index if exists ix_user_nationality_nationality_id;

drop table if exists destination;

drop table if exists destination_traveller_type;

drop table if exists destination_edit_request;

drop table if exists destination_edit_request_traveller_type;

drop table if exists destination_photo;

drop table if exists nationality;

drop table if exists personal_photo;

drop table if exists traveller_type;

drop table if exists trip;

drop table if exists trip_destination;

drop table if exists user;

drop table if exists user_traveller_type;

drop table if exists user_nationality;

