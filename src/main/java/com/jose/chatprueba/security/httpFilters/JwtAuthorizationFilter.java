package com.jose.chatprueba.security.httpFilters;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.services.DetallesUsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


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
        //String bearerToken = request.getHeader(JwtProvider.TOKEN_HEADER);
        StringBuffer bearerToken = new StringBuffer();
        Arrays.stream(request.getCookies())
            .filter(x->JwtProvider.TOKEN_HEADER.equals(x.getName()))
                //.filter(Cookie::isHttpOnly)
            .map(Cookie::getValue)
            .findFirst()
            .ifPresent(bearerToken::append);

//        List<Cookie> cookies = Arrays.stream(request.getCookies())
//            .filter(x->JwtProvider.TOKEN_HEADER.equals(x.getName()))
//            .collect(Collectors.toList());
//        cookies.forEach(cookie -> {
//            System.out.println(cookie.isHttpOnly());
//            if(cookie.isHttpOnly()){
//                bearerToken.append(cookie.getValue());
//            }
//        });

        System.out.println("JwtAuthorizationFilter: "+ bearerToken);

        if(StringUtils.hasText(bearerToken) && bearerToken.toString().startsWith(JwtProvider.TOKEN_PREFIX))
            return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());
        else
            return null;
    }
}


