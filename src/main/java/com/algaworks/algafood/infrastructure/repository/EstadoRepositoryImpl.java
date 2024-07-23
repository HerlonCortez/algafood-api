package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class EstadoRepositoryImpl implements EstadoRepository{

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Estado> listar() {
		return manager.createQuery("from Estado", Estado.class).getResultList();
	}

	@Override
	public Estado buscar(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Estado salvar(Estado estado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remover(Estado estado) {
		// TODO Auto-generated method stub
		
	}
	
	
}
