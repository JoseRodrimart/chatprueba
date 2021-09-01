package com.jose.chatprueba.security.jwt;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.UserRole;
import com.jose.chatprueba.services.UsuarioServices;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Clase encargada de generar, validar y gestionar los token JWT
 */
@Log
@Component
@RequiredArgsConstructor
public class JwtProvider {
    /**
     * Nombre de la cabecera/cookie donde debe venir el token.
     */
    public static final String TOKEN_HEADER = "Authorization";
    /**
     * Prefijo con el que comienza el token.
     */
    public static final String TOKEN_PREFIX = "Bearer";
    /**
     * Tipo de token.
     */
    public static final String TOKEN_TYPE = "JWT";
    @Value("${jwt.token-expiration}")
    private int jwtDurationTokenEnSegundos;
    private Key key;

    private final UsuarioServices detallesUsuarioServices;

    @Autowired
    private void setKey(@Value("${jwt.secret}") String jwtSecreto){
        this.key = Keys.hmacShaKeyFor(jwtSecreto.getBytes());
    }

    /**
     * Genera un token válido en base a un objeto Authentication
     *
     * @param authentication authentication
     * @return token jwt sin prefijo
     */
    public String generateToken(Authentication authentication){
        Usuario usuario = (Usuario)authentication.getPrincipal();
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtDurationTokenEnSegundos * 1000L));
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

    /**
     * Extrae la id del usuario propietario del token.
     *
     * @param token token
     * @return id del usuario
     */
    public Integer getUserIdFromJWT(String token){
        validateToken(token);
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    /**
     * Extrae el usuario de un token jwt.
     *
     * @param token the token
     * @return the user details
     */
    public UserDetails getUsuarioFromJWT(String token){
        Integer id = getUserIdFromJWT(token);
        return detallesUsuarioServices.buscaPorId(id).get();
    }

    /**
     * Valida un token, comprobando su firma, caducidad, etc.
     *
     * @param token token jwt
     * @return boolean validez
     */
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
