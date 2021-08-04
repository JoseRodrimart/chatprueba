package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.dto.UsuarioDTO;
import com.jose.chatprueba.error.UsuarioNotFoundException;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.services.ChatServices;
import com.jose.chatprueba.services.MensajeServices;
import com.jose.chatprueba.services.UsuarioServices;
import com.jose.chatprueba.services.IFicheroServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
public class UsuarioController {

    UsuarioServices usuarioServices;
    ChatServices chatServices;
    MensajeServices mensajeServices;
    IFicheroServices iFicheroServices;

    @GetMapping("/usuario")
    public ResponseEntity<List<UsuarioDTO>> usuarios(){
        List<UsuarioDTO> lista = usuarioServices.buscaTodosDTO();
        if(lista.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            return ResponseEntity.ok(lista);
        }
    }
    @GetMapping("/usuario/{id}")
    public Usuario usuario(@PathVariable Integer id){
        return usuarioServices
                .buscaPorId(id)
                .orElseThrow(()->new UsuarioNotFoundException(id));
    }
    @PostMapping(value="/usuario", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Usuario> registraUsuario(
            @RequestPart("nuevo") Usuario usuario,
            @RequestPart("file") MultipartFile file
    ){

        String urlImagen = null;

        if(!file.isEmpty()){
            String imagen = iFicheroServices.store(file);
            urlImagen = MvcUriComponentsBuilder
                            .fromMethodName(
                                    FicherosController.class,
                                    "serveFile",
                                    imagen)
                            .build()
                            .toUriString();
        }

        usuario.setImagen(urlImagen);

        Usuario guardado = usuarioServices.registra(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
    @PutMapping("/usuario/{id}")
    public Usuario editaUsuario(@RequestBody Usuario usuario, @PathVariable Integer id){
        Usuario u = usuarioServices.buscaPorId(id).orElseThrow(()->new UsuarioNotFoundException(id));
        u.setNombre(usuario.getNombre());
        u.setMail(usuario.getMail());
        u.setPass(usuario.getPass());

        return u;
    }
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Usuario> borrarUsuario(@PathVariable Integer id){
        Usuario u = usuarioServices.buscaPorId(id).orElseThrow(()->new UsuarioNotFoundException(id));
        usuarioServices.elimina(id);
        return ResponseEntity.noContent().build();
    }
}
