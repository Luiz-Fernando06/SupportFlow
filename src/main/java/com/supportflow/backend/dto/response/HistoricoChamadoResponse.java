package com.supportflow.backend.dto.response;

import com.supportflow.backend.enums.StatusChamado;

import java.time.LocalDateTime;

public record HistoricoChamadoResponse(
        Long id,
        StatusChamado statusAnterior,
        StatusChamado statusNovo,
        UsuarioResponse alteradoPor,
        LocalDateTime criadoEm
) {}
