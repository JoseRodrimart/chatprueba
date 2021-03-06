package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.dto.CreateUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.converter.UsuarioDTOConverter;
import com.jose.chatprueba.exceptions.UsuarioNotFoundException;
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
    UsuarioDTOConverter usuarioDTOConverter;

    @GetMapping("/usuario")
    public ResponseEntity<List<GetUsuarioDTO>> usuarios(){
        List<GetUsuarioDTO> lista = usuarioServices.buscaTodosDTO();
        if(lista.isEmpty())
            return ResponseEntity.notFound().build();
        else{
            return ResponseEntity.ok(lista);
        }
    }
    @GetMapping("/usuarioCompleto/{id}")
    public Usuario usuarioCompleto(@PathVariable Integer id){
        Usuario u = usuarioServices.buscaPorId(id).orElseThrow(()->new UsuarioNotFoundException(id));
        return u;
    }
    @GetMapping("/usuario/{id}")
    public GetUsuarioDTO usuario(@PathVariable Integer id){
        return usuarioServices.convierteDTO(id);
    }

    @PostMapping(value="/usuario", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GetUsuarioDTO> registraUsuario(
            @RequestPart("nuevo") CreateUsuarioDTO usuario,
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

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTOConverter.convertToDTO(usuarioServices.registra(usuario)));
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
