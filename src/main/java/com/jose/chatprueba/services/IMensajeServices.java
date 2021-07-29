package com.jose.chatprueba.services;

import com.jose.chatprueba.models.Chat;
import com.jose.chatprueba.models.Mensaje;
import java.util.List;
import java.util.Optional;

public interface IMensajeServices {
    public Optional<List<Mensaje>> mensajesUsuarioChat(Integer id_usuario, Integer id_chat);
    public boolean enviarMensaje(Integer id_chat, Integer  id_usuario, String texto);
    public Optional<List<Mensaje>> mensajesNoVistos(Integer id_usuario);
}
