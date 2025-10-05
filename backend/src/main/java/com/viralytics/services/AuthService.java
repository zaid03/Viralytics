package com.viralytics.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viralytics.models.Users;
import com.viralytics.repositories.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users register(String email, String rawPassword, String name){
        String normalized = email.trim().toLowerCase();
        if (userRepository.findByEmail(normalized).isPresent()) {
            throw new IllegalArgumentException("email exists");
        }
        Users u = new Users();
        u.setEmail(normalized);
        u.setName(name);

        if (rawPassword != null && !rawPassword.isBlank()) {
            u.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            u.setPassword(null);
        }

        return userRepository.save(u);
    }

    public boolean authenticate(String email, String rawPassword) {
        String normalized = email.trim().toLowerCase();
        Optional<Users> opt = userRepository.findByEmail(normalized);
        return opt.map(u -> {
            if (u.getPassword() == null) {
                return false;
            }
            return passwordEncoder.matches(rawPassword, u.getPassword());
        }).orElse(false);
    }
}
