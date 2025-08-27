--liquibase formatted sql
--changeset gisela:202508271602
--comment: alter unblock_reason to nullable

ALTER TABLE
    blocked_cards
MODIFY COLUMN
    unblock_reason VARCHAR(255) NULL;

--rollback ALTER TABLE blocked_cards MODIFY COLUMN unblock_reason VARCHAR(255) NOT NULL;