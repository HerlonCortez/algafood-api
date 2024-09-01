package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CadastroRestauranteService {

    private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser romovido, pois está em uso";

    @Autowired
    private RestauranteRepository restauranteRepository;


    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;
    
    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Long cidadeId = restaurante.getEndereco().getCidade().getId();

        Cozinha cozinha = cadastroCozinhaService.buscar(cozinhaId);
        Cidade cidade = cadastroCidadeService.buscar(cidadeId);
        
        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);

        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public void remover(Long restauranteId) {
        try {
            buscar(restauranteId);
            restauranteRepository.deleteById(restauranteId);
            restauranteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_RESTAURANTE_EM_USO, restauranteId));
        }
    }

    public Restaurante buscar(@PathVariable Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }
    
    @Transactional
    public void ativar(Long restauranteId) {
    	Restaurante restauranteAtual = buscar(restauranteId);
    	restauranteAtual.ativar();
    }
    
    @Transactional
    public void inativar(Long restauranteId) {
    	Restaurante restauranteAtual = buscar(restauranteId);
    	restauranteAtual.inativar();
    }
}
