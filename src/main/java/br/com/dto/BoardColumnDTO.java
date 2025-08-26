package br.com.dto;

import br.com.persistence.entity.BoardColumnTypeEnum;

public record BoardColumnDTO(
        Long id, String name, BoardColumnTypeEnum type, int cardsAmount
) {}
