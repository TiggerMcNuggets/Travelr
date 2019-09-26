# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table album (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  name                          varchar(255),
  associated_destination_id     bigint,
  is_permanent                  boolean,
  deleted                       boolean default false not null,
  constraint uq_album_associated_destination_id unique (associated_destination_id),
  constraint pk_album primary key (id)
);

create table album_media (
  album_id                      bigint not null,
  media_id                      bigint not null,
  constraint pk_album_media primary key (album_id,media_id)
);

create table comment (
  id                            bigint auto_increment not null,
  message                       varchar(255),
  timestamp                     bigint,
  trip_node_id                  bigint,
  user_id                       bigint,
  deleted                       boolean default false not null,
  constraint pk_comment primary key (id)
);

create table comment_emoji (
  id                            bigint auto_increment not null,
  emoji                         varchar(255),
  comment_id                    bigint,
  deleted                       boolean default false not null,
  constraint pk_comment_emoji primary key (id)
);

create table comment_emoji_user (
  comment_emoji_id              bigint not null,
  user_id                       bigint not null,
  constraint pk_comment_emoji_user primary key (comment_emoji_id,user_id)
);

create table destination (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  latitude                      double not null,
  longitude                     double not null,
  type                          varchar(255) not null,
  district                      varchar(255) not null,
  country                       varchar(255) not null,
  default_album_id              bigint,
  user_id                       bigint,
  is_public                     boolean default false not null,
  deleted                       boolean default false not null,
  constraint uq_destination_default_album_id unique (default_album_id),
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

create table file (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  filepath                      varchar(255),
  extension                     varchar(255),
  trip_id                       bigint,
  user_id                       bigint,
  deleted                       boolean default false not null,
  constraint pk_file primary key (id)
);

create table grouping (
  id                            bigint auto_increment not null,
  name                          varchar(250),
  description                   varchar(300),
  slack_workspace_domain        varchar(300),
  deleted                       boolean default false not null,
  constraint pk_grouping primary key (id)
);

create table media (
  id                            bigint auto_increment not null,
  is_public                     boolean default 0 not null,
  caption                       varchar(250),
  uri_string                    varchar(255) not null,
  media_type                    varchar(255) not null,
  user_id                       bigint,
  deleted                       boolean default false not null,
  constraint pk_media primary key (id)
);

create table media_album (
  media_id                      bigint not null,
  album_id                      bigint not null,
  constraint pk_media_album primary key (media_id,album_id)
);

create table nationality (
  id                            bigint auto_increment not null,
  is_old                        boolean default 0 not null,
  name                          varchar(255),
  deleted                       boolean default false not null,
  constraint pk_nationality primary key (id)
);

create table node (
  dtype                         varchar(31) not null,
  id                            bigint auto_increment not null,
  name                          varchar(255),
  arrival_date                  integer not null,
  departure_date                integer not null,
  ordinal                       integer not null,
  parent_id                     bigint,
  user_id                       bigint,
  user_group_id                 bigint,
  deleted                       boolean default false not null,
  destination_id                bigint,
  default_album_id              bigint,
  constraint uq_node_default_album_id unique (default_album_id),
  constraint pk_node primary key (id)
);

create table node_user_status (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  trip_id                       bigint,
  trip_status                   integer,
  deleted                       boolean default false not null,
  constraint ck_node_user_status_trip_status check ( trip_status in (0,1,2)),
  constraint pk_node_user_status primary key (id)
);

create table personal_photo (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  photo_filename                varchar(255) not null,
  is_public                     boolean default 0 not null,
  deleted                       boolean default false not null,
  constraint pk_personal_photo primary key (id)
);

create table slack_user (
  id                            bigint auto_increment not null,
  user_id                       bigint not null,
  access_token                  varchar(300) not null,
  deleted                       boolean default false not null,
  constraint uq_slack_user_user_id unique (user_id),
  constraint pk_slack_user primary key (id)
);

create table traveller_type (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  deleted                       boolean default false not null,
  constraint pk_traveller_type primary key (id)
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
  slack_user_id                 bigint,
  deleted                       boolean default false not null,
  constraint uq_user_email unique (email),
  constraint uq_user_slack_user_id unique (slack_user_id),
  constraint pk_user primary key (id)
);

create table user_traveller_type (
  user_id                       bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_user_traveller_type primary key (user_id,traveller_type_id)
);

create table user_group (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  grouping_id                   bigint,
  is_owner                      boolean not null default false not null,
  deleted                       boolean default false not null,
  constraint pk_user_group primary key (id)
);

create table user_nationality (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  nationality_id                bigint,
  has_passport                  boolean not null default false not null,
  deleted                       boolean default false not null,
  constraint pk_user_nationality primary key (id)
);

create index ix_album_user_id on album (user_id);
alter table album add constraint fk_album_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table album add constraint fk_album_associated_destination_id foreign key (associated_destination_id) references destination (id) on delete restrict on update restrict;

create index ix_album_media_album on album_media (album_id);
alter table album_media add constraint fk_album_media_album foreign key (album_id) references album (id) on delete restrict on update restrict;

create index ix_album_media_media on album_media (media_id);
alter table album_media add constraint fk_album_media_media foreign key (media_id) references media (id) on delete restrict on update restrict;

create index ix_comment_trip_node_id on comment (trip_node_id);
alter table comment add constraint fk_comment_trip_node_id foreign key (trip_node_id) references node (id) on delete restrict on update restrict;

create index ix_comment_user_id on comment (user_id);
alter table comment add constraint fk_comment_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_comment_emoji_comment_id on comment_emoji (comment_id);
alter table comment_emoji add constraint fk_comment_emoji_comment_id foreign key (comment_id) references comment (id) on delete restrict on update restrict;

create index ix_comment_emoji_user_comment_emoji on comment_emoji_user (comment_emoji_id);
alter table comment_emoji_user add constraint fk_comment_emoji_user_comment_emoji foreign key (comment_emoji_id) references comment_emoji (id) on delete restrict on update restrict;

create index ix_comment_emoji_user_user on comment_emoji_user (user_id);
alter table comment_emoji_user add constraint fk_comment_emoji_user_user foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table destination add constraint fk_destination_default_album_id foreign key (default_album_id) references album (id) on delete restrict on update restrict;

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

create index ix_file_trip_id on file (trip_id);
alter table file add constraint fk_file_trip_id foreign key (trip_id) references node (id) on delete restrict on update restrict;

create index ix_file_user_id on file (user_id);
alter table file add constraint fk_file_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_media_user_id on media (user_id);
alter table media add constraint fk_media_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_media_album_media on media_album (media_id);
alter table media_album add constraint fk_media_album_media foreign key (media_id) references media (id) on delete restrict on update restrict;

create index ix_media_album_album on media_album (album_id);
alter table media_album add constraint fk_media_album_album foreign key (album_id) references album (id) on delete restrict on update restrict;

create index ix_node_parent_id on node (parent_id);
alter table node add constraint fk_node_parent_id foreign key (parent_id) references node (id) on delete restrict on update restrict;

create index ix_node_user_id on node (user_id);
alter table node add constraint fk_node_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_node_user_group_id on node (user_group_id);
alter table node add constraint fk_node_user_group_id foreign key (user_group_id) references grouping (id) on delete restrict on update restrict;

create index ix_node_destination_id on node (destination_id);
alter table node add constraint fk_node_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

alter table node add constraint fk_node_default_album_id foreign key (default_album_id) references album (id) on delete restrict on update restrict;

create index ix_node_user_status_user_id on node_user_status (user_id);
alter table node_user_status add constraint fk_node_user_status_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_node_user_status_trip_id on node_user_status (trip_id);
alter table node_user_status add constraint fk_node_user_status_trip_id foreign key (trip_id) references node (id) on delete restrict on update restrict;

create index ix_personal_photo_user_id on personal_photo (user_id);
alter table personal_photo add constraint fk_personal_photo_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table slack_user add constraint fk_slack_user_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user add constraint fk_user_slack_user_id foreign key (slack_user_id) references slack_user (id) on delete restrict on update restrict;

create index ix_user_traveller_type_user on user_traveller_type (user_id);
alter table user_traveller_type add constraint fk_user_traveller_type_user foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_traveller_type_traveller_type on user_traveller_type (traveller_type_id);
alter table user_traveller_type add constraint fk_user_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_user_group_user_id on user_group (user_id);
alter table user_group add constraint fk_user_group_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_group_grouping_id on user_group (grouping_id);
alter table user_group add constraint fk_user_group_grouping_id foreign key (grouping_id) references grouping (id) on delete restrict on update restrict;

create index ix_user_nationality_user_id on user_nationality (user_id);
alter table user_nationality add constraint fk_user_nationality_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_nationality_nationality_id on user_nationality (nationality_id);
alter table user_nationality add constraint fk_user_nationality_nationality_id foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;


# --- !Downs

alter table album drop constraint if exists fk_album_user_id;
drop index if exists ix_album_user_id;

alter table album drop constraint if exists fk_album_associated_destination_id;

alter table album_media drop constraint if exists fk_album_media_album;
drop index if exists ix_album_media_album;

alter table album_media drop constraint if exists fk_album_media_media;
drop index if exists ix_album_media_media;

alter table comment drop constraint if exists fk_comment_trip_node_id;
drop index if exists ix_comment_trip_node_id;

alter table comment drop constraint if exists fk_comment_user_id;
drop index if exists ix_comment_user_id;

alter table comment_emoji drop constraint if exists fk_comment_emoji_comment_id;
drop index if exists ix_comment_emoji_comment_id;

alter table comment_emoji_user drop constraint if exists fk_comment_emoji_user_comment_emoji;
drop index if exists ix_comment_emoji_user_comment_emoji;

alter table comment_emoji_user drop constraint if exists fk_comment_emoji_user_user;
drop index if exists ix_comment_emoji_user_user;

alter table destination drop constraint if exists fk_destination_default_album_id;

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

alter table file drop constraint if exists fk_file_trip_id;
drop index if exists ix_file_trip_id;

alter table file drop constraint if exists fk_file_user_id;
drop index if exists ix_file_user_id;

alter table media drop constraint if exists fk_media_user_id;
drop index if exists ix_media_user_id;

alter table media_album drop constraint if exists fk_media_album_media;
drop index if exists ix_media_album_media;

alter table media_album drop constraint if exists fk_media_album_album;
drop index if exists ix_media_album_album;

alter table node drop constraint if exists fk_node_parent_id;
drop index if exists ix_node_parent_id;

alter table node drop constraint if exists fk_node_user_id;
drop index if exists ix_node_user_id;

alter table node drop constraint if exists fk_node_user_group_id;
drop index if exists ix_node_user_group_id;

alter table node drop constraint if exists fk_node_destination_id;
drop index if exists ix_node_destination_id;

alter table node drop constraint if exists fk_node_default_album_id;

alter table node_user_status drop constraint if exists fk_node_user_status_user_id;
drop index if exists ix_node_user_status_user_id;

alter table node_user_status drop constraint if exists fk_node_user_status_trip_id;
drop index if exists ix_node_user_status_trip_id;

alter table personal_photo drop constraint if exists fk_personal_photo_user_id;
drop index if exists ix_personal_photo_user_id;

alter table slack_user drop constraint if exists fk_slack_user_user_id;

alter table user drop constraint if exists fk_user_slack_user_id;

alter table user_traveller_type drop constraint if exists fk_user_traveller_type_user;
drop index if exists ix_user_traveller_type_user;

alter table user_traveller_type drop constraint if exists fk_user_traveller_type_traveller_type;
drop index if exists ix_user_traveller_type_traveller_type;

alter table user_group drop constraint if exists fk_user_group_user_id;
drop index if exists ix_user_group_user_id;

alter table user_group drop constraint if exists fk_user_group_grouping_id;
drop index if exists ix_user_group_grouping_id;

alter table user_nationality drop constraint if exists fk_user_nationality_user_id;
drop index if exists ix_user_nationality_user_id;

alter table user_nationality drop constraint if exists fk_user_nationality_nationality_id;
drop index if exists ix_user_nationality_nationality_id;

drop table if exists album;

drop table if exists album_media;

drop table if exists comment;

drop table if exists comment_emoji;

drop table if exists comment_emoji_user;

drop table if exists destination;

drop table if exists destination_traveller_type;

drop table if exists destination_edit_request;

drop table if exists destination_edit_request_traveller_type;

drop table if exists destination_photo;

drop table if exists file;

drop table if exists grouping;

drop table if exists media;

drop table if exists media_album;

drop table if exists nationality;

drop table if exists node;

drop table if exists node_user_status;

drop table if exists personal_photo;

drop table if exists slack_user;

drop table if exists traveller_type;

drop table if exists user;

drop table if exists user_traveller_type;

drop table if exists user_group;

drop table if exists user_nationality;

