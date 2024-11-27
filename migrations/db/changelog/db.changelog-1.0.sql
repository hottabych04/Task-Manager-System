--liquibase formatted sql

--changeset hottabych04:1
CREATE TABLE IF NOT EXISTS t_user
(
    id                BIGSERIAL,
    c_email           TEXT UNIQUE NOT NULL,
    c_hashed_password TEXT        NOT NULL,

    PRIMARY KEY (id)
);

--changeset hottabych04:2
CREATE TABLE IF NOT EXISTS t_role
(
    id     SERIAL,
    c_name TEXT UNIQUE NOT NULL,

    PRIMARY KEY (id)
);

--changeset hottabych04:3
CREATE TABLE IF NOT EXISTS t_role_to_user
(
    id      SERIAL,
    id_role INT REFERENCES t_role (id),
    id_user BIGINT REFERENCES t_user (id),

    PRIMARY KEY (id)
);

--changeset hottabych04:4
CREATE TABLE IF NOT EXISTS t_status
(
    id     SERIAL,
    c_name TEXT UNIQUE NOT NULL,

    PRIMARY KEY (id)
);

--changeset hottabych04:5
CREATE TABLE IF NOT EXISTS t_priority
(
    id     SERIAL,
    c_name TEXT UNIQUE NOT NULL,

    PRIMARY KEY (id)
);

--changeset hottabych04:6
CREATE TABLE IF NOT EXISTS t_task
(
    id            BIGSERIAL,
    c_name        TEXT NOT NULL,
    c_description TEXT,
    id_status     INT REFERENCES t_status (id),
    id_priority   INT REFERENCES t_priority (id),
    id_author     BIGINT REFERENCES t_user (id),

    PRIMARY KEY (id)
);

--changeset hottabych04:7
CREATE TABLE IF NOT EXISTS t_comment
(
    id        BIGSERIAL,
    c_message TEXT NOT NULL,
    id_author BIGINT REFERENCES t_user (id),
    id_task   BIGINT REFERENCES t_task (id),

    PRIMARY KEY (id)
);

--changeset hottabych04:8
CREATE TABLE IF NOT EXISTS t_user_to_task
(
    id      BIGSERIAL,
    id_user BIGINT REFERENCES t_user (id),
    id_task BIGINT REFERENCES t_task (id),

    PRIMARY KEY (id)
);
