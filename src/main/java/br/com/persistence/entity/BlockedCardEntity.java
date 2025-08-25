package br.com.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockedCardEntity {
    private Long id;
    private String blockReason;
    private OffsetDateTime blockedAt;
    private String unblockReason;
    private OffsetDateTime unblockedAt;
}
