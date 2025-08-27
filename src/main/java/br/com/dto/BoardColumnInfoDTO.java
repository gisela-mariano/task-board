package br.com.dto;

import br.com.persistence.entity.BoardColumnTypeEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnTypeEnum type) {
}
