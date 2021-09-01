package com.jose.chatprueba.controller;

//import com.jose.chatprueba.websocket.UsuariosActivos;
import com.jose.chatprueba.models.Chat;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.services.ChatServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    @Autowired ChatServices chatServices;

    @Autowired private SimpUserRegistry usuariosEnLinea; //Bean autoinyectado por Spring

    @Autowired private SimpMessagingTemplate messagingTemplate; //Bean autoinyectado por Spring

//    @MessageMapping("/grupos")
//    @SendTo("/broker/mensajes")
//    public String enviaMensaje(String mensaje){
//        System.out.println("Mensaje recibido: "+ mensaje);
//        return mensaje;
//    }

    @MessageMapping("/grupos/{grupoID}")
    public void mensajeSimple(@DestinationVariable Integer grupoID, String mensaje) {
        System.out.println("WebSocketController: mensaje recibido en el grupo: "+grupoID);
        List<Integer> usuariosConectados = null;
        Optional<Chat> chat = chatServices.buscaPorId(grupoID);
        if(chat.isPresent())
            usuariosConectados = usuariosEnLinea
                                .getUsers()
                                .stream()
                                .map(x->((Usuario)x.getPrincipal()).getId())
                                .collect(Collectors.toList());

            for (Usuario usuario : chat.get().getUsuarios()) {
                if(usuariosEnLinea.getUser(usuario.getNombre())!=null)
                //if (usuariosConectados.contains(usuario.getId()))

                    messagingTemplate.convertAndSend("broker/conexion/"+usuario.getId(),mensaje);
            }
    }


    /**
     * Suscripción al broker general /conexion
     * TODO: debe devolver todos los mensajes recibidos por el usuario desde su última conexión
     *
     * @return
     */
    @SubscribeMapping("/conexion")
    public Object conexionGeneral(){
        return null;
    }
}
