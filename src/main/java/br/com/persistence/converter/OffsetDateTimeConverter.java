package br.com.persistence.converter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class OffsetDateTimeConverter {

    public  static OffsetDateTime toOffsetDateTime(final Timestamp value) {
        return OffsetDateTime.ofInstant(value.toInstant(), UTC);
    }
}
