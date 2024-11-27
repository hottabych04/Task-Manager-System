--liquibase formatted sql

--changeset hottabych04:9
INSERT INTO t_role(c_name)
VALUES ('ROLE_ADMIN');
INSERT INTO t_role(c_name)
VALUES ('ROLE_USER');

--changeset hottabych04:10
INSERT INTO t_status(c_name)
VALUES ('WAIT');
INSERT INTO t_status(c_name)
VALUES ('IN_PROGRESS');
INSERT INTO t_status(c_name)
VALUES ('DONE');

--changeset hottabych04:11
INSERT INTO t_priority(c_name)
VALUES ('LOW');
INSERT INTO t_priority(c_name)
VALUES ('MIDDLE');
INSERT INTO t_priority(c_name)
VALUES ('HIGH');