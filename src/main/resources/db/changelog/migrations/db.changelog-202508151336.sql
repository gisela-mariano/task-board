--liquibase formatted sql
--changeset gisela:202508151336
--comment: create boards table

CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

--rollback DROP TABLE boards