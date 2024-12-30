--liquibase formatted sql

--changeset hottabych04:10
INSERT INTO t_role(c_name)
VALUES ('ROLE_ADMIN');
INSERT INTO t_role(c_name)
VALUES ('ROLE_USER');

--changeset hottabych04:11
INSERT INTO t_status(c_name)
VALUES ('WAIT');
INSERT INTO t_status(c_name)
VALUES ('IN_PROGRESS');
INSERT INTO t_status(c_name)
VALUES ('DONE');

--changeset hottabych04:12
INSERT INTO t_priority(c_name)
VALUES ('LOW');
INSERT INTO t_priority(c_name)
VALUES ('MIDDLE');
INSERT INTO t_priority(c_name)
VALUES ('HIGH');

--changeset hottabych04:13
INSERT INTO t_user(c_email, c_hashed_password)
VALUES ('admin@example.com', '$2a$12$BuYhqqxi2V6lSi8WAOwwy.qDMWek3z4Edffn7e4zbAbzfJv5EAweG');

--changeset hottabych04:14
INSERT INTO t_role_to_user(id_user, id_role)
SELECT t_user.id, t_role.id
FROM t_user
JOIN t_role ON t_role.c_name LIKE 'ROLE_ADMIN'
WHERE t_user.c_email LIKE 'admin@example.com';