package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStoregeService {

	InputStream recuperar(String nomeArquivo);
	
	void armazenar(NovaFoto novaFoto);
	
	void remover(String nomeArquivo);
	
	default void substituir(String nomeArquivoExistente, NovaFoto novaFoto) {
		this.armazenar(novaFoto);
		if (nomeArquivoExistente != null) {
			this.remover(nomeArquivoExistente);
		}
	}
	
	default String gerarNome(String nome) {
		return UUID.randomUUID() + "_"+ nome;
	}
	
	@Builder
	@Getter
	class NovaFoto{
		private String nomeArquivo;
		private InputStream inputStream;
	}
}
