package com.jose.chatprueba.websocket;

import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.jwt.JwtProvider;
import com.jose.chatprueba.services.DetallesUsuarioServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.session.Session;
//import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.swing.text.html.Option;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    JwtProvider tokenProvider;
    @Autowired HttpHandshakeInterceptor httpHandshakeInterceptor;
    @Autowired
    DetallesUsuarioServices detallesUsuarioServices;

    //Configuracion Global Websockets
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
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

    //Autorización de entrada

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    System.out.println("configureClientInboundChannel:usuario conectando...");
//
//                    Optional<String> token = Optional.ofNullable(accessor.getFirstNativeHeader("Authorization"));
//                    if (token.isPresent()) {
//
//                        System.out.println("extraido token: "+token.get());
//                        String jwt = token.get().substring(JwtProvider.TOKEN_PREFIX.length());;
//
//                        if(tokenProvider.validateToken(jwt)) {
//
//                            System.out.println("token validado");
//
//                            Usuario usuario = (Usuario)tokenProvider.getUsuarioFromJWT(jwt);
//
//                            UsernamePasswordAuthenticationToken authenticationToken =
//                                    new UsernamePasswordAuthenticationToken(usuario,usuario.getRoles(), usuario.getAuthorities());
//
//                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//                            System.out.println("introduciendo en la autentificación del canal stomp al usuario: "+usuario.getUsername());
//
//                            accessor.setUser(authenticationToken);
//                        }
//                    }
//                }
//                return message;
//            }
//        });
//    }

    //EventListeners
    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
        //System.out.println("webSocketConfig: [Connected] " + event.getUser().toString());
        //StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //System.out.println("webSocketConfig: [Connected] " + sha.getUser().getName() +": "+ sha.getSessionId());
    }

    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        //StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //System.out.println("webSocketConfig: [Disconnected] " + sha.getUser().getName() +": "+ sha.getSessionId());
    }
}
