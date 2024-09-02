package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class CadastroUsuarioService {

	private static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser romovido, pois está em uso ";
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario buscar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		usuarioRepository.detach(usuario);
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(String.format("Já existe um usuário cadastrado com este email %s: ", usuario.getEmail()));
		}
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void remover(Long usaurioId) {
		try {
			buscar(usaurioId);
			usuarioRepository.deleteById(usaurioId);
			usuarioRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, usaurioId));
		}
	}
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscar(usuarioId);
		
		if (usuario.senhaNaoCoincidocom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		
		usuario.setSenha(novaSenha);
	}
}
