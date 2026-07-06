package com.supportflow.backend.dto.request;


import jakarta.validation.constraints.*;

public record CriarUsuarioRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(
                min = 8,
                max = 20,
                message = "A senha deve ter no mínimo de 8 a 20 caracteres"
        )
        String senha
) {}
