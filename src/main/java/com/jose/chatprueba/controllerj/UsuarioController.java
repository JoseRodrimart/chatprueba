package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.models.Chat;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.services.ChatServices;
import com.jose.chatprueba.services.MensajeServices;
import com.jose.chatprueba.services.UsuarioServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class UsuarioController {

    UsuarioServices usuarioServices;
    ChatServices chatServices;
    MensajeServices mensajeServices;

    @GetMapping("/usuario")
    public List<Usuario> usuarios(){
        return usuarioServices.buscaTodos();
    }
    @GetMapping("/usuario/{id}")
    public Optional<Usuario> usuario(@PathVariable Integer id){
        return usuarioServices.buscaPorId(id);
    }
    @PostMapping("/usuario")
    public Usuario registraUsuario(@RequestBody Usuario usuario){
        return usuarioServices.registra(usuario);
    }
    @PutMapping("/usuario/{id}")
    public Usuario editaUsuario(@RequestBody Usuario usuario, @PathVariable Integer id){
        if(usuarioServices.compruebaPorId(id)){
            usuario.setId(id);
            return usuarioServices.registra(usuario);
        }else{
            return null;
        }
    }
    @DeleteMapping("/usuario/{id}")
    public Usuario borrarUsuario(@PathVariable Integer id){
        if(usuarioServices.compruebaPorId(id)){
            Usuario usuario = usuarioServices.buscaPorId(id).get();
            usuarioServices.elimina(id);
            return usuario;
        } else {
            return null;
        }
    }
}
