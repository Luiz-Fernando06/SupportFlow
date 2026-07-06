package com.supportflow.backend.dto.response;

import com.supportflow.backend.enums.Prioridade;
import com.supportflow.backend.enums.StatusChamado;

import java.time.LocalDateTime;

public record ChamadoResponse(
        Long id,
        String titulo,
        String descricao,
        StatusChamado status,
        Prioridade prioridade,
        CategoriaResponse categoria,
        UsuarioResponse usuario,
        UsuarioResponse adminResponsavel,
        SalaDeAtendimentoResponse sala,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
