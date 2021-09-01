package com.jose.chatprueba.security.httpFilters;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;

import com.jose.chatprueba.services.UsuarioServices;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * <h1>Filtro HTTP</h1> encargado de extraer el token jwt desde las cookies de la request y conceder autorización
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    //Falta hacer una gestión manual de la excepción de Token no encontrado
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().equals("/auth/login")){ //Omitimos el filtro cuando el usuario quiere logearse
            try {
                String token = getJwtFromRequest(request).orElseThrow(() -> new Exception("No se ha encotrado el token jwt"));
                Usuario usuario = (Usuario) jwtProvider.getUsuarioFromJWT(token);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario, usuario.getRoles(), usuario.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception ex) {
                System.err.println("No se ha podido establecer la autentificación del ususario");
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(x->JwtProvider.TOKEN_HEADER.equals(x.getName()))
            //.filter(Cookie::isHttpOnly) //No consigo identificar si la cookie era originalmente de tipo HttpOnly. Ver si es posible
            .map(Cookie::getValue)
            .filter(x->x.startsWith(JwtProvider.TOKEN_PREFIX))
            .findFirst();
    }
}


