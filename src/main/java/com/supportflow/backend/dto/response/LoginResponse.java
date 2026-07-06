package com.supportflow.backend.dto.response;

public record LoginResponse(
        String token,
        String tipo,
        UsuarioResponse usuario
) {}
