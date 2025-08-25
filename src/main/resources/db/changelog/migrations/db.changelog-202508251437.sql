--liquibase formatted sql
--changeset gisela:202508251437
--comment: create blocked_cards table

CREATE TABLE blocked_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_reason VARCHAR(255) NOT NULL,
    blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    unblock_reason VARCHAR(255) NOT NULL,
    unblocked_at TIMESTAMP NULL,
    card_id BIGINT NOT NULL,

    CONSTRAINT cards__blocked_cards_fk FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE blocked_cards