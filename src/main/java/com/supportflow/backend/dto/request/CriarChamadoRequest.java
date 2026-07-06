package com.supportflow.backend.dto.request;

import com.supportflow.backend.enums.Prioridade;
import com.supportflow.backend.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarChamadoRequest(

        @NotBlank(message = "O titulo é obrigatório")
        String titulo,

        @NotBlank(message = "É necessário uma descrição do problema")
        String descricao,

        @NotNull(message = "Preencha uma prioridade")
        Prioridade prioridade,

        @NotNull(message = "Preencha uma categoria")
        Categoria categoriaId

) {}
