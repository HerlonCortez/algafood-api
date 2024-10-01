package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	FotoRecuperada recuperar(String nomeArquivo);

	void armazenar(NovaFoto novaFoto);

	void remover(String nomeArquivo);

	default void substituir(String nomeArquivoExistente, NovaFoto novaFoto) {
		this.armazenar(novaFoto);
		if (nomeArquivoExistente != null) {
			this.remover(nomeArquivoExistente);
		}
	}

	default String gerarNome(String nome) {
		return UUID.randomUUID() + "_" + nome;
	}

	@Builder
	@Getter
	class NovaFoto {
		private String nomeArquivo;
		private String contentType;
		private InputStream inputStream;
	}

	@Builder
	@Getter
	class FotoRecuperada {
		private InputStream inputStream;
		private String url;

		public boolean temUrl() {
			return url != null;
		}

		public boolean temInputStream() {
			return inputStream != null;
		}
	}
}
