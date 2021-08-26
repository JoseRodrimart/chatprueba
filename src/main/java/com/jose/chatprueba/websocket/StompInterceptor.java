package com.jose.chatprueba.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Service
public class StompInterceptor implements ChannelInterceptor {

    @Autowired
    @Qualifier("clientOutboundChannel")
    private MessageChannel clientOutboundChannel;

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {
        System.out.println("CABEZERAS STOMP: "+message.getHeaders().toString());
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        System.out.println("StompIntercpetor.handleSessionSubscribeEvent: "+event.getMessage().getHeaders().toString());
        if(event.getMessage().getHeaders().get("simpDestination").equals("/conexion")){
            System.out.println("StompIntercpetor.handleSessionSubscribeEvent: "
                    + event.getUser().toString()
                    + "Se ha suscrito a /conexion");
        }
        System.out.println("StompInterceptor.handleSessionSubscribeEvent: " +
                "Se ha suscrito el usuario " + event.getUser().getName() + " a " + event.getMessage().getHeaders().get("simpDestination"));
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        //System.out.println("StompInterceptor.handleSessionConnectedEvent: " +SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        //StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);

//        headerAccessor.setMessage("no perteneces a este chat");
//        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
        //System.out.println("StompInterceptor.handleSessionConnectedEvent: "+ event.getMessage().getHeaders().toString());
        //System.out.println("StompInterceptor.handleSessionConnectedEvent: ");
//        System.out.println("StompInterceptor.handleSessionConnectedEvent: " +
//                "Conectado via STOMP el usuario: "+ event.getUser().getName());
    }
}
