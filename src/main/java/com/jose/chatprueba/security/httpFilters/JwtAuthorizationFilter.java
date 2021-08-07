package com.jose.chatprueba.security.httpFilters;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.services.DetallesUsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtProvider jwtProvider;
    private final DetallesUsuarioServices detallesUsuarioServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String token = getJwtFromRequest(request);
            if(StringUtils.hasText(token) && jwtProvider.validateToken(token)){
                Integer id = jwtProvider.getUserIdFromJWT(token);

                Usuario usuario = (Usuario)detallesUsuarioServices.loadUsersById(id);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario,usuario.getRoles(), usuario.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception ex){
            System.err.println("No se ha podido establecer la autentificación del ususario");
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(JwtProvider.TOKEN_HEADER);

        if(StringUtils.hasText( bearerToken) && bearerToken.startsWith(JwtProvider.TOKEN_PREFIX))
            return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());
        else
            return null;
    }
}


