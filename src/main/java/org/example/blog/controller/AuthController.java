package org.example.blog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.blog.dto.AuthResponseDTO;
import org.example.blog.dto.LoginRequestDTO;
import org.example.blog.dto.RegisterRequestDTO;
import org.example.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}
