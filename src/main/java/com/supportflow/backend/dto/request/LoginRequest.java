package com.supportflow.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha Obrigatória")
        @Size(
                min = 8,
                max = 20,
                message = "A senha deve ter de 8 a 20 caracteres"
        )
        String senha
) {}
