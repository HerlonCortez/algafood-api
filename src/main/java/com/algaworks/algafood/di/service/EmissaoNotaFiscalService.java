package com.algaworks.algafood.di.service;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.modelo.Produto;
import com.algaworks.algafood.di.notificacao.Notificador;

public class EmissaoNotaFiscalService{

	private Notificador notificador;
	
	public EmissaoNotaFiscalService(Notificador notificador) {
		this.notificador = notificador;
	}
	
	public void emitir(Cliente cliente, Produto produto) {
		// TODO emite a nota fiscal aqui...
		
		this.notificador.notificar(cliente, "Nota fiscal do produto "
				+ produto.getNome() + " foi emitida!");
	}
}
