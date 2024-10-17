package com.algaworks.algafood.api.v1.controller;

import java.util.List;

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

import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoModelAssembler estadoModelAssembler;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;
	
    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<EstadoModel> listar() {
        return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
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
    public EstadoModel buscar(@PathVariable Long estadoId) {
        return estadoModelAssembler.toModel(cadastroEstadoService.buscar(estadoId));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EstadoModel adiciona(@RequestBody @Valid EstadoInput estadoInput) {
    	Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
        return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estado));
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
    public EstadoModel alterar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {
    	
        Estado estadoAtual = cadastroEstadoService.buscar(estadoId);
        //BeanUtils.copyProperties(estado, estadoAtual, "id");
        estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
        return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estadoAtual));
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
    public void remover(@PathVariable Long estadoId) {
        cadastroEstadoService.remove(estadoId);
    }
}
