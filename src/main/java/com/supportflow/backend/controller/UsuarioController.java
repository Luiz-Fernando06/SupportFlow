package com.supportflow.backend.controller;

import com.supportflow.backend.dto.request.AtualizarEmailRequest;
import com.supportflow.backend.dto.request.AtualizarNomeRequest;
import com.supportflow.backend.dto.request.AtualizarSenhaRequest;
import com.supportflow.backend.dto.response.UsuarioResponse;
import com.supportflow.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PatchMapping("/nome")
    public ResponseEntity<UsuarioResponse> atualizarNome(
            @Valid @RequestBody AtualizarNomeRequest request) {

        return ResponseEntity.ok(
                usuarioService.atualizarNome(request)
        );
    }

    @PatchMapping("/email")
    public ResponseEntity<UsuarioResponse> atualizarEmail(
            @Valid @RequestBody AtualizarEmailRequest request) {

        return ResponseEntity.ok(
                usuarioService.atualizarEmail(request)
        );
    }

    @PatchMapping("/senha")
    public ResponseEntity<Void> alterarSenha(
            @Valid @RequestBody AtualizarSenhaRequest request) {

        usuarioService.alterarSenha(request);

        return ResponseEntity.noContent().build();
    }
}
