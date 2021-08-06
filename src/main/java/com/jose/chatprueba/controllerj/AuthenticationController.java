package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTOToken;
import com.jose.chatprueba.dto.converter.UsuarioDTOConverter;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.security.jwt.models.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider tokenProvider;
    private final UsuarioDTOConverter converter;

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

        Usuario usuario = (Usuario)authentication.getPrincipal();
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.convertToDTOWithToken(usuario,token));
    }
//
//    @PostMapping("/auth/login")
//    public ResponseEntity<GetUsuarioDTOToken> login2( @RequestParam("username") String username, @RequestParam("password") String password){
//
//        LoginRequest loginRequest = new LoginRequest(username,password);
//
//        Authentication authentication =
//                authenticationManager
//                        .authenticate(
//                                new UsernamePasswordAuthenticationToken(
//                                        loginRequest.getUsername(),
//                                        loginRequest.getPassword()
//                                ));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Usuario usuario = (Usuario)authentication.getPrincipal();
//        String token = tokenProvider.generateToken(authentication);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(converter.convertToDTOWithToken(usuario,token));
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    public GetUsuarioDTO me(@AuthenticationPrincipal Usuario user){
        return converter.convertToDTO(user);
    }
}
