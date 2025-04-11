package com.example.todolistspring.Controller;

import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.security.JwtUtil;
import com.example.todolistspring.security.TokenBlacklist;
import com.example.todolistspring.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          TokenBlacklist tokenBlacklist) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        String result = authService.register(usuario);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuario.getUsername(),
                            usuario.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(usuario.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "message", "Login exitoso"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        tokenBlacklist.add(token);
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }
}