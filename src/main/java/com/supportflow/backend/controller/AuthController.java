package com.supportflow.backend.controller;

import com.supportflow.backend.dto.request.CriarUsuarioRequest;
import com.supportflow.backend.dto.request.LoginRequest;
import com.supportflow.backend.dto.response.LoginResponse;
import com.supportflow.backend.dto.response.UsuarioResponse;
import com.supportflow.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse registrar( @Valid @RequestBody CriarUsuarioRequest request ) {

        return authService.registrar(request);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));

    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(Authentication authentication) {

        UsuarioResponse usuario = authService.buscarUsuarioLogado(authentication);

        return ResponseEntity.ok(usuario);

    }
}
