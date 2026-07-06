package com.supportflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EnviarMensagemRequest(
   @NotBlank
   String conteudo
) {}
