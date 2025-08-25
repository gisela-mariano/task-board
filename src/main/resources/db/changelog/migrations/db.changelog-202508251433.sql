--liquibase formatted sql
--changeset gisela:202508251433
--comment: create cards table

CREATE TABLE cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    board_column_id BIGINT NOT NULL,

    CONSTRAINT boards_columns__cards_fk FOREIGN KEY (board_column_id) REFERENCES board_columns (id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE cards