package com.supportflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarNomeRequest(
        @NotBlank(message = "Nome inválido!")
        String nome
) {
}
