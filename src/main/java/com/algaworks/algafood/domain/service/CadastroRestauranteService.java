package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

    private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser romovido, pois está em uso";

    @Autowired
    private RestauranteRepository restauranteRepository;


    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;
    
    @Autowired
    private CadastroCidadeService cadastroCidadeService;
    
    @Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
    
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

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
    
    @Transactional
    public void ativar(List<Long> restauranteIds) {
    	restauranteIds.forEach(this::ativar);
    }
    
    @Transactional
    public void inativar(List<Long> restauranteIds) {
    	restauranteIds.forEach(this::inativar);
    }
    
    @Transactional
    public void abrir(Long restauranteId) {
    	Restaurante restauranteAtual = buscar(restauranteId);
    	restauranteAtual.abrir();
    }
    
    @Transactional
    public void fechar(Long restauranteId) {
    	Restaurante restauranteAtual = buscar(restauranteId);
    	restauranteAtual.fechar();
    }
    
    @Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscar(formaPagamentoId);
		
		restaurante.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscar(formaPagamentoId);
		
		restaurante.adicionarFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void adicionarResponsavel(Long restauranteId, Long Usuario) {
		Restaurante restaurante = buscar(restauranteId);
		Usuario usuario = cadastroUsuarioService.buscar(Usuario);
		
		restaurante.adicionaResponsanvel(usuario);
	}
	
	@Transactional
	public void removerResponsavel(Long restauranteId, Long Usuario) {
		Restaurante restaurante = buscar(restauranteId);
		Usuario usuario = cadastroUsuarioService.buscar(Usuario);
		
		restaurante.removeResponsanvel(usuario);
	}
}
