package com.supportflow.backend.controller;

import com.supportflow.backend.dto.response.HistoricoChamadoResponse;
import com.supportflow.backend.service.HistoricoDeChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historicos")
@RequiredArgsConstructor
public class HistoricoChamadoController {

    private final HistoricoDeChamadoService historicoDeChamadoService;

    @GetMapping("/chamado/{Id}")
    public ResponseEntity<List<HistoricoChamadoResponse>> listarPorChamado(
            @PathVariable Long Id) {

        return ResponseEntity.ok(
                historicoDeChamadoService.listarPorChamado(Id)
        );
    }
}