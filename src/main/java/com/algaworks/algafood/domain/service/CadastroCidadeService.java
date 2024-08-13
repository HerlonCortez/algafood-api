package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

    private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser romovida, pois está em uso ";

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();

        cidade.setEstado(cadastroEstadoService.buscar(estadoId));

        return cidadeRepository.save(cidade);
    }

    public void remover(Long cidadeId) {
        try {
            buscar(cidadeId);
            cidadeRepository.deleteById(cidadeId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, cidadeId));
        }
    }

    public Cidade buscar(@PathVariable Long cidadeId) {
        return cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
    }
}
