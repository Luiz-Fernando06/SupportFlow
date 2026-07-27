package com.supportflow.backend.controller;

import com.supportflow.backend.dto.response.SalaDeAtendimentoResponse;
import com.supportflow.backend.service.SalaDeAtendimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/salas")
@RequiredArgsConstructor
public class SalaDeAtendimentoController {

    private final SalaDeAtendimentoService salaDeAtendimentoService;

    @GetMapping("/chamado/{chamadoId}")
    public ResponseEntity<SalaDeAtendimentoResponse> buscarPorChamado(
            @PathVariable Long chamadoId) {

        return ResponseEntity.ok(
                salaDeAtendimentoService.buscarPorChamado(chamadoId)
        );
    }

}
