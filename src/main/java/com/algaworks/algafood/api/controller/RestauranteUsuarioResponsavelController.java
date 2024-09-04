package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@GetMapping
	public List<UsuarioModel> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);
		
		return usuarioModelAssembler.toCollectionModel(restaurante.getUsuarios());
	}
	
	@PutMapping("/{usuarioId}")
	public void adicionarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		cadastroRestauranteService.adicionarResponsavel(restauranteId, usuarioId);
	}
	
	@DeleteMapping("/{usuarioId}")
	public void removerResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		cadastroRestauranteService.removerResponsavel(restauranteId, usuarioId);
	}
}
