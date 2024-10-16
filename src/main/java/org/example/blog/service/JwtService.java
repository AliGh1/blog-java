package org.example.blog.service;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public JwtService(@Value("${security.jwt.certs.public}") String publicKeyPath,
                      @Value("${security.jwt.certs.private}") String privateKeyPath) throws Exception {
        this.publicKey = KeyUtils.loadPublicKey(publicKeyPath);
        this.privateKey = KeyUtils.loadPrivateKey(privateKeyPath);
    }

    @Value("${security.jwt.access-expiration-time}")
    private long accessJwtExpiration;

    @Getter
    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshJwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public long calculateTokenRemainingValidity(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.getTime() - System.currentTimeMillis();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String generateAccessToken(UserDetails userDetails, String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return generateAccessToken(claims, userDetails, id);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails, String id) {
        extraClaims.put("type", "access");
        return buildToken(extraClaims, userDetails, accessJwtExpiration, id);
    }

    public String generateRefreshToken(UserDetails userDetails, String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return generateRefreshToken(claims, userDetails, id);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails, String id) {
        extraClaims.put("type", "refresh");
        return buildToken(extraClaims, userDetails, refreshJwtExpiration, id);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, String id) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .id(id)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey)
                .compact();
    }
}
