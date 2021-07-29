package com.jose.chatprueba.services;

import com.jose.chatprueba.models.Chat;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IChatServices {
    public Optional<List<Chat>> buscaPorUsuario(Integer id_usuario);
    public boolean registraUsuariosEnChat(Integer id_chat, Integer ... id_usuario);
    public boolean creaChat(Integer ... id_usuario);
    public Map<Integer,Integer> chatsConMensajesSinLeer(Integer id_usuario);
    Optional<List<Chat>> buscaPorEmail(String email);
}
