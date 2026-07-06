package com.supportflow.backend.dto.response;

import java.time.LocalDateTime;

public record MensagemResponse(
        Long id,
        UsuarioResponse remetente,
        String conteudo,
        LocalDateTime enviadaEm
) {}
