package org.fawry.moviesapp.controller;

import jakarta.validation.Valid;
import org.fawry.moviesapp.Service.AuthService;
import org.fawry.moviesapp.config.JwtUtil;
import org.fawry.moviesapp.dto.LoginDto;
import org.fawry.moviesapp.entity.User;
import org.fawry.moviesapp.utile.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(authToken);
            Role role = Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority());
            String token = jwtUtil.generateToken(authentication.getName(), role);
            return ResponseEntity.ok(Map.of("token", token, "role", role));
        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid input data"));
        }
        try {
            String token = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token, "role", user.getRole()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username already taken"));
        }
    }
    }