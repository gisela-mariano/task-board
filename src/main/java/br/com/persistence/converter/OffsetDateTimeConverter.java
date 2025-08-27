package br.com.persistence.converter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class OffsetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(final Timestamp value) {
        return nonNull(value) ? OffsetDateTime.ofInstant(value.toInstant(), UTC) : null;
    }
}
