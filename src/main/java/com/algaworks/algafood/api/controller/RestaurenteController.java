package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/restaurantes")
public class RestaurenteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("/{restauranteId}")
	public Restaurante buscar(@PathVariable Long restauranteId) {
		return cadastroRestauranteService.buscar(restauranteId);
	}

	@GetMapping("/taxa")
	public List<Restaurante> taxa(BigDecimal taxaIni, BigDecimal taxaFin) {
		return restauranteRepository.findByTaxaFreteBetween(taxaIni, taxaFin);
	}

	@GetMapping("/nome")
	public Optional<Restaurante> nomeUnico(String nome) {
		return restauranteRepository.findFirstByNomeContaining(nome);
	}

	@GetMapping("/nome-parcial")
	public List<Restaurante> nomeParcial(String nome, Long id) {
		return restauranteRepository.consultarPorNome(nome, id);
	}

	@GetMapping("/nome-taxa")
	public List<Restaurante> restaurantesPorNomeFrete(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {
		return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
	}

	@GetMapping("/frete-gratis")
	public List<Restaurante> restaurantesFreteGratis(String nome) {
		return restauranteRepository.findFreteGratis(nome);

	}

	@GetMapping("/primeiro")
	public Optional<Restaurante> restaurantesBuscarPrimeiro() {
		return restauranteRepository.buscarPrimeiro();

	}

	@GetMapping("/top2")
	public List<Restaurante> restaurantesTop2PorNome(String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}

	@GetMapping("/count")
	public int restaurantesCountPorCozinha(Long cozinhaId) {
		return restauranteRepository.countByCozinhaId(cozinhaId);
	}

	@PostMapping
	public Restaurante adicionar(@RequestBody Restaurante restaurante) {
		try {
			return cadastroRestauranteService.salvar(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

//	@DeleteMapping("/{restauranteId}")
//	public ResponseEntity<Restaurante> remover(@PathVariable Long restauranteId){
//		try {
//			cadastroRestauranteService.remover(restauranteId);
//			return ResponseEntity.noContent().build();
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
//	}

	@DeleteMapping("/{restauranteId}")
	public void remover(@PathVariable Long restauranteId) {
		cadastroRestauranteService.remover(restauranteId);
	}

//	@PutMapping("/{restauranteId}")
//	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
//			@RequestBody Restaurante restaurante) {
//		try {
//			Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
//			
//			if (restauranteAtual.isPresent()) {
//				BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id", "formasPagamento", "endereco","dataCadastro");
//				
//				Restaurante restauranteNovo = cadastroRestauranteService.salvar(restauranteAtual.get());
//				return ResponseEntity.ok(restauranteNovo);
//			}
//			
//			return ResponseEntity.notFound().build();
//		
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.badRequest()
//					.body(e.getMessage());
//		}
//	}

	@PutMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		Restaurante restauranteAtual = cadastroRestauranteService.buscar(restauranteId);
		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro");
		try {
			return cadastroRestauranteService.salvar(restauranteAtual);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

//	@PatchMapping("/{restauranteId}")
//	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
//			@RequestBody Map<String, Object> campos) {
//		Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
//		if (restauranteAtual.isEmpty() ) {
//			return ResponseEntity.notFound().build();
//		}
//		
//		merge(campos, restauranteAtual.get());
//		
//		return atualizar(restauranteId, restauranteAtual.get());
//	}

	@PatchMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {
		Restaurante restauranteAtual = cadastroRestauranteService.buscar(restauranteId);

		merge(campos, restauranteAtual, request);

		return atualizar(restauranteId, restauranteAtual);
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);

			camposOrigem.forEach((nomePropriedade, valorProriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, servletServerHttpRequest);
		}
	}
}
