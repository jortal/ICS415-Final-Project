# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user_info (
  name                      varchar(255) not null,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_user_info primary key (name))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table user_info;

SET FOREIGN_KEY_CHECKS=1;

