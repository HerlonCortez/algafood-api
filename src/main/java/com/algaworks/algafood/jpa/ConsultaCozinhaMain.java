package com.algaworks.algafood.jpa;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;

public class ConsultaCozinhaMain {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		CadastroCozinha cadastroCozinha = applicationContext.getBean(CadastroCozinha.class);
		cadastroCozinha.add(new Cozinha("Japonesa"));
		Cozinha cozinha1 = cadastroCozinha.buscar(1L);
		
		System.out.println(cozinha1.getNome());
		
		List<Cozinha> cozinhas = cadastroCozinha.listar();
		for (Cozinha cozinha : cozinhas) {
			System.out.println("Nome: "+ cozinha.getNome());
		}
		
		cozinha1.setId(1L);
		cozinha1.setNome("Brasileira");
		
		cadastroCozinha.add(cozinha1);
		
		cozinhas = cadastroCozinha.listar();
		System.out.println("----");
		for (Cozinha cozinha : cozinhas) {
			System.out.println("Nome: "+ cozinha.getNome());
		}
		cadastroCozinha.remover(cozinha1);
		
		cozinhas = cadastroCozinha.listar();
		System.out.println("----");
		for (Cozinha cozinha : cozinhas) {
			System.out.println("Nome: "+ cozinha.getNome());
		}
	}
}
