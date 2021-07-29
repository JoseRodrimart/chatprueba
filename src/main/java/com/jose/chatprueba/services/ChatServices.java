package com.jose.chatprueba.services;

import com.jose.chatprueba.models.Chat;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ChatServices implements IServices<Chat> , IChatServices{

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UsuarioServices usuarioServices;

    //Funciones comunes a los servicios

    @Override
    public boolean compruebaPorId(Integer id) {
        return chatRepository.existsById(id);
    }
    @Override
    public List<Chat> buscaTodos(){
        return chatRepository.findAll();
    }
    @Override
    public Optional<Chat> buscaPorId(Integer id) {
        return chatRepository.findById(id);
    }
    @Override
    public Chat registra(Chat chat){
        return chatRepository.save(chat);
    }
    @Override
    public boolean registra(Chat ... chat) {
        try{
            Arrays.stream(chat).forEach(chatRepository::save);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void elimina(Chat chat) {
        chatRepository.delete(chat);
    }
    @Override
    public void elimina(Integer id) {
        chatRepository.deleteById(id);
    }

    //Funciones de IChatServices
    @Override
    public Optional<List<Chat>> buscaPorUsuario(Integer id_usuario) {
        return chatRepository.buscaPorUsuario(id_usuario);
    }
    @Override
    public boolean registraUsuariosEnChat(Integer id_chat, Integer... id_usuario) {
        try{
            Arrays.stream(id_usuario).forEach(id-> chatRepository.getById(id_chat).agregaUsuario(usuarioServices.buscaPorId(id).get()));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Se ha producido un error registrando los usuarios en el chat");
            return false;
        }
    }
    @Override
    public boolean creaChat(Integer ... id_usuario) {
            try {
                Chat c = new Chat();
                registra(c);
                registraUsuariosEnChat(c.getId(), id_usuario);
                return true;
            }catch(Exception e){
                e.printStackTrace();
                System.err.println("Se ha producido un error registrando el chat");
                return false;
            }
    }
    @Override
    public Map<Integer,Integer> chatsConMensajesSinLeer(Integer id_usuario) {
        List<Map<Integer,Integer>> lista = chatRepository.chatsConMensajesSinLeer(id_usuario).get();
        Map<Integer,Integer> map = new HashMap<>();
        lista.forEach(x->{
            map.put(x.get("0"),x.get("1"));
        });
        System.out.println("");
        return map;
    }

    @Override
    public Optional<List<Chat>> buscaPorEmail(String email) {
        return chatRepository.buscaPorEmail(email);
    }

}
