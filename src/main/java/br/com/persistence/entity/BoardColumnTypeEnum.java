package br.com.persistence.entity;

import java.util.stream.Stream;

public enum BoardColumnTypeEnum {
    INITIAL,
    FINAL,
    CANCEL,
    PENDING;

    public static BoardColumnTypeEnum findByName(final String name) {
        return Stream.of(BoardColumnTypeEnum.values())
                     .filter(type -> type.name().equals(name))
                     .findFirst()
                     .orElseThrow();
    }
}
