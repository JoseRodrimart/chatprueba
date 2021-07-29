package com.jose.chatprueba.services;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class UsuarioServices implements IServices<Usuario>, IUsuarioServices{
    @Autowired
    UsuarioRepository usuarioRepository;

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
        return usuarioRepository.save(usuario);
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
}
