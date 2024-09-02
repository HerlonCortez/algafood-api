package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoException  extends EntidadeNaoEncontradaException{

	 private static final long serialVersionUID = 1L;

	    public UsuarioNaoEncontradoException(String mensagem) {
	        super(mensagem);
	    }

	    public UsuarioNaoEncontradoException(Long restauranteId) {
	        this(String.format("Não existe um usuário cadastrado com o código %d", restauranteId));
	    }
}
