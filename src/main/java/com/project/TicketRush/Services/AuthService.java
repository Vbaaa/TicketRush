package com.project.TicketRush.Services;

import com.project.TicketRush.auth.JwtService;
import com.project.TicketRush.auth.*;
import com.project.TicketRush.auth.dto.LoginRequest;
import com.project.TicketRush.auth.dto.RegisterRequest;
import com.project.TicketRush.auth.dto.TokenResponse;
import com.project.TicketRush.common.ConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @Transactional
    public void register(RegisterRequest r) {
        String email = r.email().trim().toLowerCase();
        if (users.existsByEmail(email)) throw new ConflictException("Email already registered");
        User u = new User();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(r.password()));
        u.setFullName(r.fullName());
        u.setRole(Role.USER);               // nobody can self-register as ADMIN/ORGANIZER
        users.save(u);
    }

    public TokenResponse login(LoginRequest r) {
        User u = users.findByEmail(r.email().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!encoder.matches(r.password(), u.getPasswordHash()))
            throw new BadCredentialsException("Invalid email or password");
        return new TokenResponse(jwt.generate(u), jwt.expirySeconds());
    }
}
