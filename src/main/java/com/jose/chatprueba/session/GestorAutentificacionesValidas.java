package com.jose.chatprueba.session;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class GestorAutentificacionesValidas implements AuthenticationSuccessHandler {
//
//    AlmacenUsuariosActivos almacenUsuariosActivos;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        HttpSession session = request.getSession(false);
//
//        if (session != null) {
//            UsuarioActivo usuarioActivo = new UsuarioActivo(authentication.getName(), almacenUsuariosActivos);
//            session.setAttribute("user", usuarioActivo);
//        }
//    }
//}
