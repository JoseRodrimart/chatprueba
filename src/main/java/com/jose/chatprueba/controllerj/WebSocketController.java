package com.jose.chatprueba.controllerj;

import com.jose.chatprueba.models.MensajeSimple;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/status")
    @SendTo("/topic/messages")
    public MensajeSimple enviaMensaje(String mensaje){
        System.out.println("Mensaje recibido: "+ mensaje);
        return new MensajeSimple("jose",mensaje);
    }

    @MessageMapping("/grupos/{grupoID}")
    @SendTo("/broker/mensajes/{grupoID}")
    public MensajeSimple mensajeSimple(@DestinationVariable Integer grupoID, String mensaje, String usuario) {
        return new MensajeSimple(usuario,mensaje);
    }
}
