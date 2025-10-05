package com.viralytics.controllers;

import com.viralytics.models.Users;
import com.viralytics.services.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            Users created = authService.register(req.email, req.password, req.name);
            return ResponseEntity.status(201).body(new RegisterResponse(created.getId(), created.getEmail(), created.getName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        boolean ok = authService.authenticate(req.email, req.password);

        if (!ok) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials"));
        }

        return ResponseEntity.ok(new LoginResponse("ok"));
    }

    public static class RegisterRequest { public String email; public String password; public String name; }
    public static class RegisterResponse {
        public Integer id; public String email; public String name;
        public RegisterResponse(Integer id, String email, String name){ this.id = id; this.email = email; this.name = name; }
    }
    public static class LoginRequest { public String email; public String password; }
    public static class LoginResponse { public String status; public LoginResponse(String status){ this.status = status; } }
    public static class ErrorResponse { public String error; public ErrorResponse(String error){ this.error = error; } }

}
