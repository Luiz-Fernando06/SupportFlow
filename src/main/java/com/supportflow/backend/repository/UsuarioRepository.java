package com.supportflow.backend.repository;

import com.supportflow.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioByEmail (String email);
}
