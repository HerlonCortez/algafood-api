package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser romovido, pois está em uso";
	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe um código de restaurante com o código %d";
	private static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um código de cozinha com o código %d";

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinhaService.buscar(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}
	
	public void remover(Long restauranteId) {
		try {
			buscar(restauranteId);
			restauranteRepository.deleteById(restauranteId);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_RESTAURANTE_EM_USO, restauranteId));
		}
	}
	
	public Restaurante buscar(@PathVariable Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId)));
	}
}
