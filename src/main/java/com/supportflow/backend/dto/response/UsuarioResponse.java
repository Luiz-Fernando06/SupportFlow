package com.supportflow.backend.dto.response;

import com.supportflow.backend.enums.Role;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Role role,
        LocalDateTime criadoEm
) {}
