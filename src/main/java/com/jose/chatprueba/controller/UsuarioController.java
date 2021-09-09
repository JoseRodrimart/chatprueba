package com.jose.chatprueba.controller;

import com.jose.chatprueba.dto.CreateUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.converter.UsuarioDTOConverter;
import com.jose.chatprueba.exceptions.UsuarioNotFoundException;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.UserRole;
import com.jose.chatprueba.services.ChatServices;
import com.jose.chatprueba.services.MensajeServices;
import com.jose.chatprueba.services.UsuarioServices;
import com.jose.chatprueba.services.IFicheroServices;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
public class UsuarioController {

    UsuarioServices usuarioServices;
    ChatServices chatServices;
    MensajeServices mensajeServices;
    IFicheroServices iFicheroServices;
    UsuarioDTOConverter usuarioDTOConverter;


    @Cacheable("usuarios")
    @GetMapping("/usuario")
    public ResponseEntity<List<GetUsuarioDTO>> usuarios(@AuthenticationPrincipal Usuario user){
        System.out.println("Lista de usuarios leida de la base de datos");
        //System.out.println(user.getId());
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
    public Usuario editaUsuario(@RequestBody GetUsuarioDTO usuario, @PathVariable Integer id){
        Usuario u = usuarioServices.buscaPorId(id).orElseThrow(()->new UsuarioNotFoundException(id));
        u.setNombre(usuario.getNombre());
        u.setMail(usuario.getMail());
        Set<String> roles = usuario.getRoles();
        Set<UserRole> rolesEnum = (Set<UserRole>) roles.stream().map(UserRole::valueOf);
        u.setRoles(rolesEnum);
        usuarioServices.registra(u);
        return u;
    }
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Usuario> borrarUsuario(@PathVariable Integer id){
        Usuario u = usuarioServices.buscaPorId(id).orElseThrow(()->new UsuarioNotFoundException(id));
        usuarioServices.elimina(id);
        return ResponseEntity.noContent().build();
    }
}
