package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTOToken;
import com.jose.chatprueba.dto.converter.UsuarioDTOConverter;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.security.jwt.models.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider tokenProvider;
    private final UsuarioDTOConverter converter;
    @Autowired private SessionRegistry sessionRegistry;

    @PostMapping("/auth/login")
    public ResponseEntity<GetUsuarioDTOToken> login(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication =
                authenticationManager
                        .authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        loginRequest.getUsername(),
                                        loginRequest.getPassword()
                                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        sessionRegistry.registerNewSession("name" , authentication.getName());
        System.out.println("Usuario " + authentication.getName() + " ingresado en sesión");

        Usuario usuario = (Usuario)authentication.getPrincipal();

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.convertToDTOWithToken(usuario,token));
    }

    @GetMapping("/auth/logout")
    public void logout(@AuthenticationPrincipal Usuario user){
        sessionRegistry.removeSessionInformation(user.getNombre());
    }

    @GetMapping("/usuariosLogeados")
    public List<Object> usuariosLogeados(){
        List<Object> lista = sessionRegistry.getAllPrincipals();
        for(Object usuario:lista){
            if(usuario instanceof Usuario)
                System.out.println(((Usuario) usuario).getNombre());
        }
        return sessionRegistry.getAllPrincipals();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    public GetUsuarioDTO me(@AuthenticationPrincipal Usuario user){
        return converter.convertToDTO(user);
    }
}
