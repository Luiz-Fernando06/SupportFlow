package com.supportflow.backend.controller;

import com.supportflow.backend.dto.request.CriarChamadoRequest;
import com.supportflow.backend.dto.response.ChamadoResponse;
import com.supportflow.backend.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<ChamadoResponse> criarChamado(@Valid @RequestBody CriarChamadoRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chamadoService.criar(request));
    }

    @GetMapping("/meu")
    public ResponseEntity<List<ChamadoResponse>> listarMeusChamados() {

        return ResponseEntity.ok(chamadoService.listarMeusChamados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponse> buscarChamado(@PathVariable Long id) {

        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ChamadoResponse> cancelarChamado(@PathVariable Long id) {

        return ResponseEntity.ok(chamadoService.cancelarChamado(id));
    }
}
