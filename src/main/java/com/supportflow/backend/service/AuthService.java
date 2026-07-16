package com.supportflow.backend.service;

import com.supportflow.backend.dto.request.CriarUsuarioRequest;
import com.supportflow.backend.dto.request.LoginRequest;
import com.supportflow.backend.dto.response.LoginResponse;
import com.supportflow.backend.dto.response.UsuarioResponse;
import com.supportflow.backend.enums.Role;
import com.supportflow.backend.exception.EmailJaCadastradoException;
import com.supportflow.backend.exception.UsuarioNaoEncontradoException;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.UsuarioRepository;

import com.supportflow.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse registrar(CriarUsuarioRequest request) {
        if (usuarioRepository.findUsuarioByEmail(request.email().trim()).isPresent()) {
            throw new EmailJaCadastradoException("Esse email já existe!");
        }

        String senhaHash = passwordEncoder.encode(request.senha());
        Usuario usuario = new Usuario(request.nome().trim(), request.email().trim(), senhaHash, Role.USER);
        usuarioRepository.save(usuario);

        return respostaDe(usuario);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(request.email().trim())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Email ou senha inválido!"));


        if(!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new UsuarioNaoEncontradoException("Email ou senha inválido!");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                respostaDe(usuario)
        );
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
