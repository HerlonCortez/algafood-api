package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.core.web.AlgaMediaTypes;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "v1/cidades", produces = AlgaMediaTypes.V1_APPLICATION_JSON_VALUE)
public class CidadeController {

	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<CidadeModel> listar() {
        return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
    }

//	@GetMapping("/{cidadeId}")
//	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId){
//		Optional<Cidade> cidade = cidadeRepository.findById(cidadeId);
//		if (cidade.isPresent()) {
//			return ResponseEntity.ok().body(cidade.get());
//		}
//		return ResponseEntity.notFound().build();
//	}

    @GetMapping("/{cidadeId}")
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        CidadeModel cidadeModel = cidadeModelAssembler.toModel(cadastroCidadeService.buscar(cidadeId));
        
//        cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).slash(cidadeModel.getId()).withSelfRel());
        cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
        		.buscar(cidadeModel.getId())).withSelfRel());
        
//        cidadeModel.add(Link.of("http://api.algafood:8080/cidades/1").withRel(IanaLinkRelations.SELF));
//        cidadeModel.add(Link.of("http://api.algafood:8080/cidades").withRel("cidades"));
        cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
        		.listar()).withRel("cidades"));
        return cidadeModel;
    }

    @PostMapping
    public CidadeModel salvar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
        	Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
            CidadeModel cidadeModel = cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidade));
            
            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
            
            return cidadeModel;
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

//	@PutMapping("/{cidadeId}") ResponseEntity<Cidade> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade){
//		Optional<Cidade> cidadeNova = cidadeRepository.findById(cidadeId);
//		
//		if (cidadeNova.isPresent()) {
//			BeanUtils.copyProperties(cidade, cidadeNova.get(),"id");
//			Cidade cidadeSalva = cadastroCidadeService.salvar(cidadeNova.get());
//			return ResponseEntity.ok().body(cidadeSalva);
//		}
//		return ResponseEntity.notFound().build();
//	}


    @PutMapping("/{cidadeId}")
    public CidadeModel atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {
        Cidade cidadeAtual = cadastroCidadeService.buscar(cidadeId);
        cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
        //BeanUtils.copyProperties(cidade, cidadeNova, "id");
        try {
            return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidadeAtual));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

//	@DeleteMapping("/{cidadeId}")
//	public ResponseEntity<Cidade> remover(@PathVariable Long cidadeId){
//		try {
//			cadastroCidadeService.remover(cidadeId);
//			return ResponseEntity.noContent().build();
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//		}
//	}

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidadeService.remover(cidadeId);
    }

}
