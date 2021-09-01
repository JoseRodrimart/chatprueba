package com.jose.chatprueba.services;

import com.jose.chatprueba.dto.CreateUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.converter.UsuarioDTOConverter;
import com.jose.chatprueba.exceptions.UsuarioConPasswordsDistintasException;
import com.jose.chatprueba.exceptions.UsuarioNotFoundException;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.repositories.UsuarioRepository;
import com.jose.chatprueba.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServices implements IServices<Usuario>, IUsuarioServices{
    UsuarioRepository usuarioRepository;
    UsuarioDTOConverter usuarioDTOConverter;
    PasswordEncoder passwordEncoder;

    //Metodos comunes de los servicios
    @Override
    public boolean compruebaPorId(Integer id) {
        return usuarioRepository.existsById(id);
    }
    @Override
    public List<Usuario> buscaTodos(){
        return usuarioRepository.findAll();
    }
    @Override
    public Optional<Usuario> buscaPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
    @Override
    public boolean registra(Usuario ... usuarios) {
        try {
            Arrays.stream(usuarios).forEach(usuarioRepository::save);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario registra(Usuario usuario) {
        return null;
    }

    @Override
    public void elimina(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
    @Override
    public void elimina(Integer id) {
        usuarioRepository.deleteById(id);
    }

    //Metodos propios de IUsusarioServices
    @Override
    public Optional<Usuario> buscaPorNombre(String nombre){
        return usuarioRepository.buscaPorNombre(nombre);
    }
    @Override
    public List<GetUsuarioDTO> buscaTodosDTO(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<GetUsuarioDTO> listaDTO = usuarios.stream().map(usuarioDTOConverter::convertToDTO).collect(Collectors.toList());
        return listaDTO;
    }
    @Override
    public Usuario buscaPorEmail(String email) {
        return usuarioRepository.buscaPorEmailCompleto(email);
    }
    @Override
    public boolean compruebaEmail(String email) {
        return false;
    }
    @Override
    public boolean compruebaPassword(String email, String pass) {
        return false;
    }
    @Override
    public List<Usuario> buscaPorChat(Integer id_chat) {
        return null;
    }
    @Override
    public boolean cambiarNombre(Integer id_usuario, String nombre) {
        try{
            Usuario usuario = buscaPorId(id_usuario).get();
            usuario.setNombre(nombre);
            return true;
        }catch (Exception e) {
            System.out.println("Ha ocurrido un error cambiando el nombre");
            e.printStackTrace();
            return false;
        }
    }

    //Metodos sin implementar en interfaces

    public GetUsuarioDTO convierteDTO(Integer id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(usuarioDTOConverter::convertToDTO).orElseThrow(()->new UsuarioNotFoundException(id));
    }

    public Usuario registra(CreateUsuarioDTO usuario) {
        if(usuario.getPass().contentEquals(usuario.getPass2())) {
            Usuario nuevo = Usuario.builder()
                    .nombre(usuario.getNombre())
                    .mail(usuario.getMail())
                    .pass(passwordEncoder.encode(usuario.getPass()))
                    .imagen(usuario.getImagen())
                    .roles(Stream.of(UserRole.USER).collect(Collectors.toSet()))
                    .build();
            try{
                return usuarioRepository.save(nuevo);
            }catch (DataIntegrityViolationException ex){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre o el mail ya están registrados");
            }
        }
        else throw new UsuarioConPasswordsDistintasException();
    }

    public UserDetails loadUserById(Integer id) throws UsuarioNotFoundException{
        return usuarioRepository.findById(id).orElseThrow(()->new UsuarioNotFoundException(id));
    }
}
