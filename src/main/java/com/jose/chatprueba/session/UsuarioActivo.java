package com.jose.chatprueba.session;

import com.jose.chatprueba.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.List;

//@Component
//@Data @RequiredArgsConstructor @AllArgsConstructor
//public class UsuarioActivo implements HttpSessionBindingListener {
//    private String nombre;
//    private AlmacenUsuariosActivos almacenUsuariosActivos;
//
//    @Override
//    public void valueBound(HttpSessionBindingEvent event) {
//        List<String> users = almacenUsuariosActivos.getUsuarios();
//        UsuarioActivo user = (UsuarioActivo) event.getValue();
//        if (!users.contains(user.getNombre())) {
//            users.add(user.getNombre());
//        }
//    }
//
//    @Override
//    public void valueUnbound(HttpSessionBindingEvent event) {
//        List<String> users = almacenUsuariosActivos.getUsuarios();
//        UsuarioActivo user = (UsuarioActivo) event.getValue();
//        if (!users.contains(user.getNombre())) {
//            users.remove(user.getNombre());
//        }
//    }
//}
