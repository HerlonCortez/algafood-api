package com.algaworks.algafood.domain.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.Pedido;

import jakarta.transaction.Transactional;

@Service
public class FluxoPedidoService {
	@Autowired
	private EmissaoPedidoService emissaoPedidoService;
	
	@Transactional
	public void confirmar(UUID codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscar(codigoPedido);
		pedido.confirmar();
	}
	
	@Transactional
	public void cancelar(UUID codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscar(codigoPedido);
		pedido.cancelar();
	}
	
	@Transactional
	public void entregar(UUID codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscar(codigoPedido);
		pedido.entregar();
	}
}
