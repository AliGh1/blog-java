package org.example.blog.service;

import org.example.blog.dto.AuthResponseDTO;
import org.example.blog.dto.LoginRequestDTO;
import org.example.blog.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}
