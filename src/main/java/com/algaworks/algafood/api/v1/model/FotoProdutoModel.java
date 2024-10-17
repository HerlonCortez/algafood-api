package com.algaworks.algafood.api.v1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoProdutoModel {

	private String nomeArquivo;
	private String descricao;
	private String contenType;
	private Long tamanho;
}
