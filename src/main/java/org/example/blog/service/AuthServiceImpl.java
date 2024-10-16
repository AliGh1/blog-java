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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        userService.saveUser(userMapper.toEntity(request));
        String uuid = generateTokenId();
        return new AuthResponseDTO(
                generateAccessToken(request.getEmail(), uuid),
                generateRefreshToken(request.getEmail(), uuid)
        );
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String uuid = generateTokenId();
        return new AuthResponseDTO(
                generateAccessToken(request.getEmail(), uuid),
                generateRefreshToken(request.getEmail(), uuid)
        );
    }

    @Override
    public AuthResponseDTO refreshToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String refreshToken = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(refreshToken);
            final String tokenType = jwtService.extractTokenType(refreshToken);
            final String tokenId = jwtService.extractId(refreshToken);

            if ("refresh".equals(tokenType) && userEmail != null) {
                UserDetails userDetails = userService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    return new AuthResponseDTO(
                            generateAccessToken(userEmail, tokenId),
                            refreshToken
                    );
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(token);
            final String tokenType = jwtService.extractTokenType(token);
            final String tokenId = jwtService.extractId(token);
            final long tokenExpiration = "refresh".equals(tokenType)
                    ? jwtService.calculateTokenRemainingValidity(token)
                    : jwtService.getRefreshJwtExpiration();

            if (userEmail != null) {
                UserDetails userDetails = userService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(token, userDetails)) {
                    jwtBlacklistService.blacklistToken(tokenId, tokenExpiration);
                    return;
                }

            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private String generateAccessToken(String email, String id) {
        return jwtService.generateAccessToken(userService.loadUserByUsername(email), id);
    }

    private String generateRefreshToken(String email, String id) {
        return jwtService.generateRefreshToken(userService.loadUserByUsername(email), id);
    }

    private String generateTokenId() {
        return UUID.randomUUID().toString();
    }
}
