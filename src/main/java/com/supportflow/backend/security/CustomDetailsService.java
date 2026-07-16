package com.supportflow.backend.security;

import com.supportflow.backend.exception.UsuarioNaoEncontradoException;
import com.supportflow.backend.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsService implements UserDetailsService{
    private final UsuarioRepository usuarioRepository;

    public CustomDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails pegarUmUsuarioPeloNome(String email) throws UsernameNotFoundException {
        return usuarioRepository
                .findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado!"));
    }
}
