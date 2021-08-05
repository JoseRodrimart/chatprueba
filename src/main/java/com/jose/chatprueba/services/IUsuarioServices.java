package com.jose.chatprueba.services;

import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioServices {
    public Usuario buscaPorEmail(String email);
    public boolean compruebaEmail(String email);
    public boolean compruebaPassword(String email, String pass);
    public List<Usuario> buscaPorChat(Integer id_chat);
    public boolean cambiarNombre(Integer id_usuario, String nombre);
    public Optional<Usuario> buscaPorNombre(String nombre);
    public List<GetUsuarioDTO> buscaTodosDTO();
}
