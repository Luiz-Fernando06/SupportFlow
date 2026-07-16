package com.supportflow.backend.service;

import com.supportflow.backend.dto.request.AtualizarEmailRequest;
import com.supportflow.backend.dto.request.AtualizarNomeRequest;
import com.supportflow.backend.dto.request.AtualizarSenhaRequest;
import com.supportflow.backend.dto.response.UsuarioResponse;
import com.supportflow.backend.exception.EmailJaCadastradoException;
import com.supportflow.backend.exception.RegraDeNegocioException;
import com.supportflow.backend.exception.UsuarioNaoEncontradoException;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse atualizarEmail(AtualizarEmailRequest request) {
        Usuario usuario = usuarioLogado();

        if (usuarioRepository.findUsuarioByEmail(request.email().trim()).isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado!");
        }

        usuario.setEmail(request.email());
        usuarioRepository.save(usuario);

        return respostaDe(usuario);
    }

    public UsuarioResponse atualizarNome(AtualizarNomeRequest request) {
        Usuario usuario = usuarioLogado();
        usuario.setNome(request.nome());
        usuarioRepository.save(usuario);
        return respostaDe(usuario);
    }

    public void alterarSenha(AtualizarSenhaRequest request) {
        Usuario usuario = usuarioLogado();

        if(!passwordEncoder.matches(request.senhaAntiga(), usuario.getSenhaHash())) {
            throw new RegraDeNegocioException("Essa senha não existe!");
        }

        if (!request.senhaNova().equals(request.confirmarSenha())) {
            throw new RegraDeNegocioException("A senha não é igual!");
        }

        usuario.setSenhaHash(passwordEncoder.encode(request.senhaNova()));
        usuarioRepository.save(usuario);
    }

    private Usuario usuarioLogado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado."));
    }

    private UsuarioResponse respostaDe(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }
}
