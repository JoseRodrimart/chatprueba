package com.jose.chatprueba.services;

import com.jose.chatprueba.models.Chat;
import com.jose.chatprueba.models.Mensaje;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.repositories.MensajeRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MensajeServices implements IServices<Mensaje>, IMensajeServices{
    @Autowired
    MensajeRepository mensajeRepository;
    @Autowired
    UsuarioServices usuarioServices;
    @Autowired
    ChatServices chatServices;

    //Metodos comunes a los servicios
    @Override
    public boolean compruebaPorId(Integer id) {
        return mensajeRepository.existsById(id);
    }
    @Override
    public List<Mensaje> buscaTodos(){
        return mensajeRepository.findAll();
    }
    @Override
    public Optional<Mensaje> buscaPorId(Integer id) {
        return mensajeRepository.findById(id);
    }
    @Override
    public boolean registra(Mensaje ... mensajes) {
        try{
            Arrays.stream(mensajes).forEach(mensajeRepository::save);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Mensaje registra(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }
    @Override
    public void elimina(Mensaje mensaje) {
        mensajeRepository.delete(mensaje);
    }
    @Override
    public void elimina(Integer id) {
        mensajeRepository.deleteById(id);
    }

    //Metodos propios de IMensajeService
    @Override
    public Optional<List<Mensaje>> mensajesUsuarioChat(Integer id_usuario, Integer id_chat) {
        Optional<Chat> chat = chatServices.buscaPorId(id_chat);
        Hibernate.initialize(chat.get().getMensajes());
        Optional<List<Mensaje>> mensajes = Optional.of(chat.get().getMensajes());
        return mensajes;
    }
    @Override
    public boolean enviarMensaje(Integer id_chat, Integer id_usuario, String texto) {
        return false;
    }
    @Override
    public Optional<List<Mensaje>> mensajesNoVistos(Integer id_usuario) {
        return mensajeRepository.mensajesNoVistos(id_usuario);
    }
}
