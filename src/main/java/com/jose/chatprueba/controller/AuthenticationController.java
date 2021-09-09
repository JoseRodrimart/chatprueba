package com.jose.chatprueba.controller;

import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTOToken;
import com.jose.chatprueba.dto.converter.UsuarioDTOConverter;
import com.jose.chatprueba.models.Chat;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.security.jwt.models.LoginRequest;
import com.jose.chatprueba.services.ChatServices;
import com.sun.net.httpserver.HttpPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.session.data.redis.RedisSessionRepository;
//import org.springframework.session.web.socket.server.SessionRepositoryMessageInterceptor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final ChatServices chatServices;
    private final JwtProvider tokenProvider;
    private final UsuarioDTOConverter converter;
    //@Autowired RedisSessionRepository sessionRepository; //Registrado en SessionConfig (Posiblemente sin uso).
    //@Autowired private SessionRegistry sessionRegistry;

    @Autowired private SimpUserRegistry userRegistry; //MUY MUY TEMPORAL, ELIMINAR

    @PostMapping("/auth/login")
    public ResponseEntity<GetUsuarioDTOToken> login(
            @Valid @RequestBody LoginRequest loginRequest
            , HttpServletResponse response
            , HttpServletRequest request
            , HttpPrincipal principal
            , HttpSession session
            ){

        //TODO: EXCEPCIONES T_T
        //==========================================//
        System.out.println("post login");
        System.out.println(loginRequest.getUsername());
        //Inicio Autentificación//

        UsernamePasswordAuthenticationToken tokenUsuarioPass =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword());

        Authentication authentication =
                authenticationManager.authenticate(tokenUsuarioPass);

        SecurityContextHolder.getContext().setAuthentication(authentication); //asignamos la autentificación al contexto
        //==========================================//


        //Inicio gestión de sesión//
        Usuario usuario = (Usuario)authentication.getPrincipal();
        String token = tokenProvider.generateToken(authentication);
        GetUsuarioDTOToken usuarioEnviar = converter.convertToDTOWithToken(usuario,token);
        //System.out.println("AuthenticationController: el contenido de GetUsuarioDTOToken es: \n: "+usuarioEnviar);
        //String sesion = request.getSession().getId();
        //sessionRegistry.registerNewSession(sesion,authentication.getPrincipal());
        //System.out.println("AuthenticationController: Lista de usuarios registrados en sesión: \n"+sessionRegistry.getAllPrincipals());
        //==========================================//


        //System.out.println("Usuario " + authentication.getName() + " ingresado en sesión");

//GESTION COOKIES
        System.out.println("AuthenticationController: el contenido del token es: "+token);
        String tokenEnvio = "Bearer" + token;
        Cookie cookieJWT = new Cookie("Authorization", tokenEnvio);
        cookieJWT.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        //cookieJWT.setSecure(true);
        cookieJWT.setHttpOnly(true);
        cookieJWT.setPath("/"); // global cookie accessible every where
        response.addCookie(cookieJWT);
        //        Optional<Cookie> cookie = Arrays.stream(request
//                .getCookies())
//                .filter(x->x.getName().equals("JSESSIONID")).findFirst();
//        if(cookie.isPresent()) {
//            System.out.println(cookie.get().getValue());
//            sessionRegistry.registerNewSession();
//        }
//        else{
//            System.out.println("No se incluye la cookie");
//        }
//        System.out.println("AuthenticationController: "+response.getHeader("Set-Cookie"));
//        String header = response.getHeader("Set-Cookie").concat("; SameSite=none; Secure");
//        response.setHeader("Set-Cookie", header);
//        System.out.println("AuthenticationController: "+response.getHeader("Set-Cookie"));

        System.out.println("AuthenticationController: "+authentication.getPrincipal());
        System.out.println("AuthenticationController: "+session.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioEnviar);
    }

//    @GetMapping("/auth/logout")
//    public void logout(@AuthenticationPrincipal Usuario user){
//        sessionRegistry.removeSessionInformation(user.getNombre());
//    }

//    @GetMapping("/usuariosLogeados")
//    public List<Object> usuariosLogeados(){
//        List<Object> lista = sessionRegistry.getAllPrincipals();
//        for(Object usuario:lista){
//            if(usuario instanceof Usuario)
//                System.out.println(((Usuario) usuario).getNombre());
//        }
//        return sessionRegistry.getAllPrincipals();
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    public GetUsuarioDTO me(@AuthenticationPrincipal Usuario user){
        return converter.convertToDTO(user);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/chats")
    public Optional<List<Chat>> chatsUsuario(@AuthenticationPrincipal Usuario user
            ,HttpServletResponse response
            ,HttpServletRequest request
    ){
        System.out.println("AuthenticationController.chatsUsuario: Sesión del usuario realizando la petición de sus chats: "+request.getSession().getId());
        System.out.println("AuthenticationController.chatsUsuario: Nombre del usuario realizando la petición de sus chats: "+request.getUserPrincipal().getName());
        System.out.println("AuthenticationController.chatsUsuario: Lista completa de usuarios con sesión abierta: NI PUTA IDEA DE COMO SACARLO !>_>! ");
        //System.out.println(user.getNombre());
        //CAHPUZA PARA VER CON ESTE METODO LOS USUARIOS CONECTADOS POR WEBSOCKET
        System.out.println("AuthenticationController.chatsUsuario: Usuarios actualmente conectados a websocket: "+userRegistry.getUserCount());
        System.out.println("Lista de usuarios conectados actualmente:");
        for (SimpUser userRegistryUser : userRegistry.getUsers()) {
            System.out.println(userRegistryUser.getName());
        }

        return chatServices.buscaPorUsuario(user.getId());
    }

}