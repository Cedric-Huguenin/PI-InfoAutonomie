# --- !Ups

create table param (
  param_key              varchar(255) not null,
  param_value            varchar(255),
  constraint pk_param primary key (param_key))
;

# --- !Downs

drop table if exists param;