package br.com.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {
    private Long id;
    private String name;
    private BoardColumnTypeEnum type;
    private int order;
}
