//package com.jose.chatprueba.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//
//@Configuration
//public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpSubscribeDestMatchers("/**")
//                .permitAll()
////                .authenticated()
//                .simpDestMatchers("/**")
////                .permitAll()
//                .authenticated()
//                .simpDestMatchers("/broker/**")
////                .permitAll()
//                .authenticated()
//                //.anyMessage().authenticated();
//                //.simpSubscribeDestMatchers("/broker/mensajes/**").authenticated()
//                ;
//
//        //.simpDestMatchers("/app/**").authenticated();
//    }
//
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true;
//    }
//}
