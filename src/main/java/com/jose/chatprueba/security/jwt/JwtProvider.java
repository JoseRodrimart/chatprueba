package com.jose.chatprueba.security.jwt;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.UserRole;
import com.jose.chatprueba.services.DetallesUsuarioServices;
import com.jose.chatprueba.services.UsuarioServices;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Component
public class JwtProvider {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String TOKEN_TYPE = "JWT";
    private Key key;

    @Autowired private DetallesUsuarioServices detallesUsuarioServices;

    @Autowired
    public void setKey(@Value("${jwt.secret}") String jwtSecreto){
        this.key = Keys.hmacShaKeyFor(jwtSecreto.getBytes());
    }


    @Value("${jwt.token-expiration}")
    private int jwtDurationTokenEnSegundos;

    public String generateToken(Authentication authentication){
        Usuario usuario = (Usuario)authentication.getPrincipal();
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtDurationTokenEnSegundos * 1000));
        return Jwts.builder()
                .signWith(key)
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
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims.toString());

        return Integer.parseInt(claims.getSubject());
    }

    public UserDetails getUsuarioFromJWT(String token){
        System.out.println("dentro");
        Integer id = getUserIdFromJWT(token);
        System.out.println("JwtProvider.getUsuarioFromJwt: identificado en el token al usuario con id: "+id);
        return detallesUsuarioServices.loadUsersById(id);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.info("Error en la firma del token JWT: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.info("Token malformado: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("El token ha expirado: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.info("Token JWT no soportado: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims vacío");
        }
        return false;
    }
}
