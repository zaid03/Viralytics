package com.viralytics.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viralytics.models.Users;
import com.viralytics.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public UserController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email, req.password)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = request.getSession(true);
        // ensure Spring Security context is saved in the session so it will be restored on subsequent requests
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                             SecurityContextHolder.getContext());

        // store minimal user info separately if you want
        authService.findByEmail(auth.getName()).ifPresent(u -> session.setAttribute("userId", u.getId()));

        return ResponseEntity.ok(new LoginResponse("ok"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        var s = request.getSession(false);
        if (s != null) s.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(Map.of("status","logged out"));
    }

    @GetMapping("/session")
    public ResponseEntity<?> session(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        HttpSession session = request.getSession(false);
        Object userId = session != null ? session.getAttribute("userId") : null;
        Map<String, Object> resp = new HashMap<>();
        resp.put("authenticated", authenticated);
        resp.put("sessionId", session != null ? session.getId() : null);
        resp.put("principal", authenticated ? auth.getName() : null);
        resp.put("userId", userId);

        return ResponseEntity.ok(resp);
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
