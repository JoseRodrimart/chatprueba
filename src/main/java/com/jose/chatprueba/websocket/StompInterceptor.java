package com.jose.chatprueba.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Service
public class StompInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {
        System.out.println("CABEZERAS STOMP: "+message.getHeaders().toString());
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        System.out.println(event.getMessage().getHeaders().toString());
        System.out.println("StompInterceptor: Usuario suscrito");
    }
    @EventListener
    public void handleSessionSubscribeEvent(SessionConnectedEvent event) {
        System.out.println(event.getMessage().getHeaders().toString());
        System.out.println("StompInterceptor: conectado el usuario: "+event.getUser());
        System.out.println("StompInterceptor: Usuario conectado");
    }
}
