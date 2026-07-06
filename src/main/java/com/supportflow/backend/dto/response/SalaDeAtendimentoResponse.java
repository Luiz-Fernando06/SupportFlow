package com.supportflow.backend.dto.response;

import java.time.LocalDateTime;

public record SalaDeAtendimentoResponse(
        Long id,
        ChamadoResumoResponse chamado,
        boolean ativa,
        LocalDateTime criadoEm
) {}
