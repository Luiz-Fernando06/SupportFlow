package com.supportflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnviarMensagemRequest(
   @NotBlank
   @Size(max = 2000, message = "A mensagem deve ter no máximo 2000 caracteres")
   String conteudo
) {}
