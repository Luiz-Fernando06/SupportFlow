package com.supportflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarSenhaRequest(
        @NotBlank(message = "A senha incorreta!")
        @Size(
                min = 8,
                max = 20,
                message = "A senha deve ter no mínimo de 8 a 20 caracteres"
        )
        String senhaAntiga,

        @NotBlank(message = "Senha inválida!")
        @Size(
                min = 8,
                max = 20,
                message = "A senha deve ter no mínimo de 8 a 20 caracteres"
        )
        String senhaNova,

        @NotBlank(message = "A senha não está igual!")
        @Size(
                min = 8,
                max = 20,
                message = "A senha deve ter no mínimo de 8 a 20 caracteres"
        )
        String confirmarSenha

) { }
