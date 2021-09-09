package com.jose.chatprueba.security.httpFilters;//package com.jose.chatprueba.security.httpFilters;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.jaas.AbstractKeycloakLoginModule;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

///**
// * <h1>Filtro HTTP</h1> encargado de extraer el token jwt desde las cookies de la request y conceder autorización
// */
@Component
@RequiredArgsConstructor
public class AuthorizationOauthToUserFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UsuarioServices usuarioServices;
    private final AuthenticationManager authenticationManager;

    //
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        System.out.println(username);

        Usuario usuario = (Usuario) usuarioServices.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario, usuario.getRoles(), usuario.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        System.out.println(authenticationToken.getPrincipal().toString());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}


