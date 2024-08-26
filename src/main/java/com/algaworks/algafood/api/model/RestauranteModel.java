package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RestauranteModel {

	private Long RestauranteId;

    private String nome;

    private BigDecimal frete;
    
    private CozinhaModel cozinha;
}
