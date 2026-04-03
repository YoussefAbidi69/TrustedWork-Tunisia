package tn.esprit.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.userservice.dto.LoginRequest;
import tn.esprit.userservice.dto.RegisterRequest;
import tn.esprit.userservice.service.IAuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Auth APIs: Register, Login, Refresh Token")
public class AuthController {

    private final IAuthService authService;

    // Endpoint totalement public : pas de token requis
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Endpoint totalement public : c'est l'étape avant d'avoir un token
    @PostMapping("/login")
    @Operation(summary = "Login user")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Endpoint public : le refresh token remplace le Bearer token expiré
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token is required"));
        }
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}