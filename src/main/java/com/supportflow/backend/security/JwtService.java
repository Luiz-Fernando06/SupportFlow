package com.supportflow.backend.security;

import com.supportflow.backend.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("role", usuario.getRole().name())
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extrairClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extrairClaims(token);

        return claimsResolver.apply(claims);
    }

    public String extrairEmail(String token) {
        return extrairClaim(
                token,
                Claims::getSubject
        );
    }

    public Date extrairExpiracao(String token) {
        return extrairClaim(
                token,
                Claims::getExpiration
        );
    }

    public boolean tokenExpirado(String token) {
        return extrairExpiracao(token)
                .before(new Date());
    }

    public boolean tokenValido(String token, UserDetails userDetails) {

        String email = extrairEmail(token);

        return email.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }


}
