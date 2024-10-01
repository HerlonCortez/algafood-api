package com.algaworks.algafood.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping("/pedidos/{codigoPedido}")
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedidoService;
	
	
	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmar(@PathVariable UUID codigoPedido) {
		fluxoPedidoService.confirmar(codigoPedido);
	}
	
	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable UUID codigoPedido) {
		fluxoPedidoService.cancelar(codigoPedido);
	}
	
	@PutMapping("/entrega")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entregar(@PathVariable UUID codigoPedido) {
		fluxoPedidoService.entregar(codigoPedido);
	}
}
