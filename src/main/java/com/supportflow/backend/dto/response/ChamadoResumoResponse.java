package com.supportflow.backend.dto.response;

import com.supportflow.backend.enums.Prioridade;
import com.supportflow.backend.enums.StatusChamado;

import java.time.LocalDateTime;

public record ChamadoResumoResponse (
        Long id,
        String titulo,
        StatusChamado status,
        Prioridade prioridade,
        CategoriaResponse categoria,
        UsuarioResponse usuario,
        UsuarioResponse adminResponsavel,
        LocalDateTime criadoEm
) {}
