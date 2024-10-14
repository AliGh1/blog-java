package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.dto.AuthResponseDTO;
import org.example.blog.dto.LoginRequestDTO;
import org.example.blog.dto.RegisterRequestDTO;
import org.example.blog.dto.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        return new AuthResponseDTO(
                generateAccessToken(request.getEmail()),
                generateRefreshToken(request.getEmail())
        );
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return new AuthResponseDTO(
                generateAccessToken(request.getEmail()),
                generateRefreshToken(request.getEmail())
        );
    }

    @Override
    public AuthResponseDTO refreshToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String refreshToken = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(refreshToken);
            final String tokenType = jwtService.extractTokenType(refreshToken);

            if ("refresh".equals(tokenType) && userEmail != null) {
                UserDetails userDetails = userService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    return new AuthResponseDTO(
                            generateAccessToken(userEmail),
                            refreshToken
                    );
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private String generateAccessToken(String email) {
        return jwtService.generateAccessToken(userService.loadUserByUsername(email));
    }

    private String generateRefreshToken(String email) {
        return jwtService.generateRefreshToken(userService.loadUserByUsername(email));
    }
}
