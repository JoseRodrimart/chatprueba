package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.models.MensajeSimple;
//import com.jose.chatprueba.websocket.UsuariosActivos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

//    @Autowired
//    UsuariosActivos usuariosActivos;

    @MessageMapping("/grupos")
    @SendTo("/broker/mensajes")
    public String enviaMensaje(String mensaje){
        System.out.println("Mensaje recibido: "+ mensaje);
        return mensaje;
    }

    @MessageMapping("/grupos/{grupoID}")
    @SendTo("/broker/mensajes/{grupoID}")
    public String mensajeSimple(@DestinationVariable Integer grupoID, String mensaje) {
        System.out.println("WebSocketController: mensaje recibido en el grupo: "+grupoID);
        return mensaje;
    }

//    @SubscribeMapping("/usuariosActivos")
//    public List<Integer> usuariosActivos(){return usuariosActivos.getUsuariosActivos();}
}
