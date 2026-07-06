package com.supportflow.backend.dto.request;

import com.supportflow.backend.enums.StatusChamado;

import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(
        @NotNull
        StatusChamado status
) {}
