package br.com.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(
        Long id,
        String title,
        String description,
        boolean isBlocked,
        OffsetDateTime blockedAt,
        String blockReason,
        int blocksAmount,
        Long columnId,
        String columnName
) {}
