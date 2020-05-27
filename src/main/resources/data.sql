drop table if exists worker;
drop table if exists project;
drop table if exists artifact;
drop table if exists task;

create table worker
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  account_not_expired BIT(1) NOT NULL,
  account_non_locked BIT(1) NOT NULL,
  credentials_not_expired BIT(1) NOT NULL,
  enabled BIT(1) NOT NULL,
  name CHARACTER varying(255) NOT NULL,
  surname CHARACTER varying(255) NOT NULL,
  login CHARACTER varying(255) NOT NULL UNIQUE,
  email CHARACTER varying(255) UNIQUE,
  password CHARACTER varying(255) NOT NULL,
  role CHARACTER varying(256) NOT NULL
);

create table project
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name CHARACTER varying(255),
  description CHARACTER varying(255),
  admin_id BIGINT,
  INDEX(admin_id)
);

create table artifact
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name CHARACTER varying(255),
  content CHARACTER varying(255),
  type CHARACTER varying(255),
  project_id BIGINT,
  INDEX(project_id)

);

create table task
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name CHARACTER varying(255),
  description CHARACTER varying(255),
  deadline DATE,
  project_id BIGINT,
  done TINYINT(1),
  INDEX(project_id)
);