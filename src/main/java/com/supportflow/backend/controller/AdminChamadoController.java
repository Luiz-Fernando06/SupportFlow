package com.supportflow.backend.controller;

import com.supportflow.backend.dto.request.AlterarStatusRequest;
import com.supportflow.backend.dto.response.ChamadoResponse;
import com.supportflow.backend.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/chamados")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminChamadoController {

    private final ChamadoService chamadoService;

    @GetMapping
    public ResponseEntity<List<ChamadoResponse>> listarTodosOsChamados() {

        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponse> verDetalhesDeUmChamado(@PathVariable Long id) {

        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/assumir")
    public ResponseEntity<ChamadoResponse> assumirChamado(@PathVariable Long id) {

        return ResponseEntity.ok(chamadoService.assumirChamado(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoResponse> alterarStatusDoChamado(@PathVariable Long id,
                                                                           @Valid @RequestBody AlterarStatusRequest request) {

        return ResponseEntity.ok(chamadoService.alterarStatus(id, request));
    }

}
