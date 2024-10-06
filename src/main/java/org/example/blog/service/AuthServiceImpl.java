package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.dto.AuthResponseDTO;
import org.example.blog.dto.LoginRequestDTO;
import org.example.blog.dto.RegisterRequestDTO;
import org.example.blog.dto.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        userService.saveUser(userMapper.toEntity(request));

        return new AuthResponseDTO(generateToken(request.getEmail()));
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return new AuthResponseDTO(generateToken(request.getEmail()));
    }

    private String generateToken(String email) {
        return jwtService.generateToken(userService.loadUserByUsername(email));
    }
}
