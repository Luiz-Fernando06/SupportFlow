package com.supportflow.backend.controller;

import com.supportflow.backend.dto.request.EnviarMensagemRequest;
import com.supportflow.backend.dto.response.MensagemResponse;
import com.supportflow.backend.service.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas/{salaId}/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;

    @PostMapping
    public ResponseEntity<MensagemResponse> enviarMensagem(
            @PathVariable Long salaId,
            @Valid @RequestBody EnviarMensagemRequest request) {

        return ResponseEntity.ok(
                mensagemService.enviar(salaId, request)
        );
    }

    @GetMapping
    public ResponseEntity<List<MensagemResponse>> listarMensagens(
            @PathVariable Long salaId) {

        return ResponseEntity.ok(
                mensagemService.listar(salaId)
        );
    }
}
