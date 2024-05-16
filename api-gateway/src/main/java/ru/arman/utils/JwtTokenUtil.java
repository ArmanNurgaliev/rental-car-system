package ru.arman.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getAuthoritiesFromToken(String token) {
        return getClaimFromToken(token, (c) -> c.get("roles")).toString();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT signature: {}", e.getMessage());
            throw new SignatureException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT token: {}", e.getMessage());
            throw new MalformedJwtException(e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is expired: {}", e.getMessage());
            throw new ExpiredJwtException(null,null,e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is unsupported: {}", e.getMessage());
            throw new UnsupportedJwtException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JwtUtils | validateJwtToken | JWT claims string is empty: {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public boolean validateToken(String token, String role) {
        return Arrays.stream(getAuthoritiesFromToken(token).split(",")).toList().contains(role);
    }
}
