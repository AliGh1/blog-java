package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.dto.AuthResponseDTO;
import org.example.blog.dto.LoginRequestDTO;
import org.example.blog.dto.RegisterRequestDTO;
import org.example.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        return null;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        return null;
    }
}
