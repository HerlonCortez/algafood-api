package com.algaworks.algafood;


import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CadastroCozinhaIntegrationTests {

    @Autowired
    CadastroCozinhaService cadastroCozinhaService;

    @Test
    void testarCadastroCozinhaSucesso() {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");

        cozinha = cadastroCozinhaService.salvar(cozinha);

        Assertions.assertThat(cozinha).isNotNull();
        Assertions.assertThat(cozinha.getId()).isNotNull();
    }

    @Test
    void testarCadastroCozinhaSemNome() {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("");

        ConstraintViolationException erroEsperado =
                org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class, () -> {
                    cadastroCozinhaService.salvar(cozinha);
                });
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso() {

        EntidadeEmUsoException erroEsperado =
                org.junit.jupiter.api.Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
                    cadastroCozinhaService.remover(1L);
                });

        Assertions.assertThat(erroEsperado).isNotNull();

    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {


        CozinhaNaoEncontradaException erroEsperado =
                org.junit.jupiter.api.Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
                    cadastroCozinhaService.remover(100L);
                });

        Assertions.assertThat(erroEsperado).isNotNull();

    }

}
