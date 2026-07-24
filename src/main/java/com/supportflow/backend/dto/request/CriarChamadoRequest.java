package com.supportflow.backend.dto.request;

import com.supportflow.backend.enums.Prioridade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarChamadoRequest(

        @NotBlank(message = "O titulo é obrigatório")
        @Size(max = 150)
        String titulo,

        @NotBlank(message = "É necessário uma descrição do problema")
        @Size(max = 5000)
        String descricao,

        @NotNull(message = "Preencha uma prioridade")
        Prioridade prioridade,

        @NotNull(message = "Preencha uma categoria")
        Long categoriaId

) {}
