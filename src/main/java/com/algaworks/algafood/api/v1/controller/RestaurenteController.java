package com.algaworks.algafood.api.v1.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.flywaydb.core.internal.util.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("v1/restaurantes")
public class RestaurenteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteModelDisassembler;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private SmartValidator validator;

	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarApenasNomes() {
		return listar();
	}

	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		return restauranteModelAssembler.toModel(cadastroRestauranteService.buscar(restauranteId));
	}

	@PostMapping
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = restauranteModelDisassembler.toDomainObject(restauranteInput);

			return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restaurante));
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{restauranteId}")
	public void remover(@PathVariable Long restauranteId) {
		cadastroRestauranteService.remover(restauranteId);
	}

	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			//Restaurante restaurante = restauranteModelDisassembler.toDomainObject(restauranteInput);
			Restaurante restauranteAtual = cadastroRestauranteService.buscar(restauranteId);
			restauranteModelDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
//			BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco",
//					"dataCadastro");
			return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PatchMapping("/{restauranteId}")
	public RestauranteModel atualizarPatch(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos,
			HttpServletRequest request) {
		Restaurante restauranteAtual = cadastroRestauranteService.buscar(restauranteId);

		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");
		
		RestauranteInput restauranteInput = modelMapper.map(restauranteAtual, RestauranteInput.class);
		
		return atualizar(restauranteId, restauranteInput);
	}

	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
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
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.inativar(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/ativos")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@RequestBody List<Long> restauranteIds) {
		cadastroRestauranteService.ativar(restauranteIds);
	}
	
	@DeleteMapping("/{restauranteId}/ativos")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@RequestBody List<Long> restauranteIds) {
		cadastroRestauranteService.inativar(restauranteIds);
	}
	
	@PutMapping("/{restauranteId}/abrir")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void aberto(@PathVariable Long restauranteId) {
		cadastroRestauranteService.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechado(@PathVariable Long restauranteId) {
		cadastroRestauranteService.fechar(restauranteId);
	}
}
