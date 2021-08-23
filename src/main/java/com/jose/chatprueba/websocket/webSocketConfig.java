package com.jose.chatprueba.websocket;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableWebSocketMessageBroker
public class webSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

    @Autowired HttpHandshakeInterceptor httpHandshakeInterceptor;

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/chat")
       //         .withSockJs()
                .setAllowedOriginPatterns(
                        "http://localhost:[*]",
                        "chrome-extension://**",
                        "file://**",
                        "https://apic.app/**")
                .addInterceptors(httpHandshakeInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/broker");
        registry.setApplicationDestinationPrefixes("/app");
    }



    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("webSocketConfig: [Connected] " + sha.getUser());
    }

    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("webSocketConfig: [Disconnected] " + sha.getUser());
    }
}
