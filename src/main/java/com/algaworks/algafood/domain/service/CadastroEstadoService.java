package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CadastroEstadoService {

    private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser romovido, pois está em uso ";

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado buscar(@PathVariable Long estadoId) {
        return estadoRepository.findById(estadoId).orElseThrow(
                () -> new EstadoNaoEncontradoException(estadoId));
    }
    
    @Transactional
    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Transactional
    public void remove(Long estadoId) {
        try {
            buscar(estadoId);
            estadoRepository.deleteById(estadoId);
            estadoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, estadoId));
        }
    }
}
