--liquibase formatted sql
--changeset gisela:202508251423
--comment: create board_columns table

CREATE TABLE board_columns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(10) NOT NULL,
    `order` int NOT NULL,
    board_id BIGINT NOT NULL,

    CONSTRAINT boards__boards_columns_fk FOREIGN KEY (board_id) REFERENCES boards (id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY unique_board_id_order (board_id, `order`)
) ENGINE=InnoDB;

--rollback DROP TABLE board_columns