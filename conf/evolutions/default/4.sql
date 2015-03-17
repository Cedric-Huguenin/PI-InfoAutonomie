# --- !Ups

create table alert (
  id                        varchar(255) not null,
  name                      varchar(255),
  duration                  integer,
  expression                varchar(255),
  icon                      varchar(255),
  color                     varchar(255),
  constraint pk_alert primary key (id))
;

create table alert_occurrence (
  id                        varchar(255) not null,
  alert_id                  varchar(255),
  timestamp                 bigint,
  date                      varchar(255),
  constraint pk_alert_occurrence primary key (id))
;

create sequence alert_seq;

create sequence alert_occurrence_seq;

alter table alert_occurrence add constraint fk_alert_occurrence_alert foreign key (alert_id) references alert (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists alert_occurrence;
drop table if exists alert;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists alert_occurrence_seq;

drop sequence if exists alert_seq;