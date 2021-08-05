package com.jose.chatprueba.security.jwt;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String TOKEN_TYPE = "JWT";

    @Value("${jwt.secret}")
    private String jwtSecreto;

    @Value("${jwt.token-expiration}")
    private int jwtDurationTokenEnSegundos;

    public String generateToken(Authentication authentication){
        Usuario usuario = (Usuario)authentication.getPrincipal();
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtDurationTokenEnSegundos * 1000);
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecreto.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ",TOKEN_TYPE)
                .setSubject(Integer.toString(usuario.getId()))
                .setIssuedAt(new Date())
                .setExpiration(tokenExpirationDate)
                .claim("fullname", usuario.getNombre())
                .claim("roles",
                            usuario.getRoles()
                                    .stream()
                                    .map(UserRole::name)
                                    .collect(Collectors.joining(", "))
                        )
                .compact();
    }

    public Integer getUserIdFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes()))
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }
}
