package com.algaworks.algafood.api.controller;

import java.io.IOException;
<<<<<<< HEAD
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
=======

import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 91092fe (commit da aula 14.11)
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.ResponseStatus;
=======
>>>>>>> 91092fe (commit da aula 14.11)
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
<<<<<<< HEAD
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
=======
>>>>>>> 91092fe (commit da aula 14.11)
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
<<<<<<< HEAD
import com.algaworks.algafood.infrastructure.service.storage.LocalFotoStoregeService;
=======
>>>>>>> 91092fe (commit da aula 14.11)

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProdutoService;
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;
	
<<<<<<< HEAD
	@Autowired
	private LocalFotoStoregeService fotoStorage;
	
=======
>>>>>>> 91092fe (commit da aula 14.11)
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		Produto produto = cadastroProdutoService.buscar(restauranteId, produtoId);
		MultipartFile arquivo = fotoProdutoInput.getArquivo();
		
		FotoProduto fotoProduto = new FotoProduto();
		fotoProduto.setProduto(produto);
		fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
		fotoProduto.setContentType(arquivo.getContentType());
		fotoProduto.setTamanho(arquivo.getSize());
		fotoProduto.setNomeArquivo(arquivo.getOriginalFilename());
		return fotoProdutoModelAssembler.toModel(catalogoFotoProdutoService.salvar(fotoProduto, arquivo.getInputStream()));
<<<<<<< HEAD
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = catalogoFotoProdutoService.buscar(restauranteId, produtoId);
		return fotoProdutoModelAssembler.toModel(fotoProduto);
	}
	
	@GetMapping
	public ResponseEntity<InputStreamResource> servirFoto(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) 
					throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = catalogoFotoProdutoService.buscar(restauranteId, produtoId);
			
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
			
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
			
			InputStream inputStream = fotoStorage.recuperar(fotoProduto.getNomeArquivo());
			
			return ResponseEntity.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(inputStream));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, 
			List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void remover(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId) {
		catalogoFotoProdutoService.excluir(restauranteId, produtoId);
	}
=======
	}
	
	@GetMapping
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = catalogoFotoProdutoService.buscar(restauranteId, produtoId);
		return fotoProdutoModelAssembler.toModel(fotoProduto);
	}
	
	
>>>>>>> 91092fe (commit da aula 14.11)
//	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public void atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
//			@Valid FotoProdutoInput fotoProdutoInput) {
//
//		var nomeArquivo = UUID.randomUUID().toString() + "_" + fotoProdutoInput.getArquivo().getOriginalFilename();
//
//		var arquivoFoto = Path.of("/home/herlon/Documentos/catalogo", nomeArquivo);
//		
//		try {
//			fotoProdutoInput.getArquivo().transferTo(arquivoFoto);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
}
