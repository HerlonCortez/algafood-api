package com.algaworks.algafood.jpa;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

public class ConsultaCozinhaMain {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		//cozinhaRepository.salvar(new Cozinha("Japonesa"));
		Cozinha cozinha1 = cozinhaRepository.buscar(1L);
		
		System.out.println(cozinha1.getNome());
		
		List<Cozinha> cozinhas = cozinhaRepository.listar();
		for (Cozinha cozinha : cozinhas) {
			System.out.println("Nome: "+ cozinha.getNome());
		}
		
		cozinha1.setId(1L);
		cozinha1.setNome("Brasileira");
		
		cozinhaRepository.salvar(cozinha1);
		
		cozinhas = cozinhaRepository.listar();
		System.out.println("----");
		for (Cozinha cozinha : cozinhas) {
			System.out.println("Nome: "+ cozinha.getNome());
		}
//		cozinhaRepository.remover(cozinha1);
		
		cozinhas = cozinhaRepository.listar();
		System.out.println("----");
		for (Cozinha cozinha : cozinhas) {
			System.out.println("Nome: "+ cozinha.getNome());
		}
	}
}
