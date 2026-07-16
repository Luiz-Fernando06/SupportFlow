package com.supportflow.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {

    UserDetails pegarUmUsuarioPeloNome(String nome) throws UsernameNotFoundException;
}
