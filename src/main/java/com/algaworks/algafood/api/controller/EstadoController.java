package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstadoService;
	
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}
	
//	@GetMapping("/{estadoId}")
//	public ResponseEntity<Estado> buscar(@PathVariable Long estadoId){
//		Optional<Estado> estado = estadoRepository.findById(estadoId);
//		if (estado.isPresent()) {
//			return ResponseEntity.ok().body(estado.get());
//		}
//		return ResponseEntity.notFound().build();
//	}
	
	@GetMapping("/{estadoId}")
	public Estado buscar(@PathVariable Long estadoId){
		return cadastroEstadoService.buscar(estadoId);
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Estado adiciona(@RequestBody Estado estado) {
		return cadastroEstadoService.salvar(estado);
	}
	
//	@PutMapping("/{estadoId}")
//	public ResponseEntity<Estado> alterar(@PathVariable Long estadoId, @RequestBody Estado estado){
//		Optional<Estado> estadoAtual = estadoRepository.findById(estadoId);
//		if (estadoAtual.isPresent()) {
//			BeanUtils.copyProperties(estado, estadoAtual,"id");
//			Estado estadoSalvo = cadastroEstadoService.salvar(estadoAtual.get());
//			return ResponseEntity.ok().body(estadoSalvo);
//		}
//		return ResponseEntity.notFound().build();
//	}
	
	@PutMapping("/{estadoId}")
	public Estado alterar(@PathVariable Long estadoId, @RequestBody Estado estado){
		Estado estadoAtual = cadastroEstadoService.buscar(estadoId);
		BeanUtils.copyProperties(estado, estadoAtual,"id");
		return cadastroEstadoService.salvar(estadoAtual);
	}
	
//	@DeleteMapping("/{estadoId}")
//	public ResponseEntity<Estado> remover(@PathVariable Long estadoId){
//		try {
//			cadastroEstadoService.remove(estadoId);
//			return ResponseEntity.noContent().build();
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
//	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId){
		cadastroEstadoService.remove(estadoId);
	}
}
