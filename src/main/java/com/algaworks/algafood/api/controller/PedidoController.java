package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EmissaoPedidoService emissaoPedidoService;

	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;

	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;

	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;

	@GetMapping
	public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable) {
		pageable = toPageble(pageable);
		
		Page<Pedido> pagePedido = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);

		List<PedidoResumoModel> resumoModels = pedidoResumoModelAssembler.toCollectionModel(pagePedido.getContent());

		return new PageImpl<>(resumoModels, pageable, pagePedido.getTotalElements());
	}

	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable UUID codigoPedido) {
		System.out.println("UUID - "+codigoPedido);
		return pedidoModelAssembler.toModel(emissaoPedidoService.buscar(codigoPedido));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
		Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

		novoPedido.setCliente(new Usuario());
		novoPedido.getCliente().setId(1L);

		return pedidoModelAssembler.toModel(emissaoPedidoService.emitir(novoPedido));
	}

	private Pageable toPageble(Pageable pageable) {
		var mapeamento = Map.of("codigo", "codigo", "restaurante.nome", "restaurante.nome", "nomeCliente",
				"cliente.nome", "valorTotal", "valorTotal");
		return PageableTranslator.translate(pageable, mapeamento);
	}
}
