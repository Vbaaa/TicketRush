package com.project.TicketRush.Controller;

import com.project.TicketRush.Services.AuthService;
import com.project.TicketRush.auth.dto.LoginRequest;
import com.project.TicketRush.auth.dto.RegisterRequest;
import com.project.TicketRush.auth.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest r) {
        service.register(r);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest r) { return service.login(r); }
}
