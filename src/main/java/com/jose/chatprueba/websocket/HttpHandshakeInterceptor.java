package com.jose.chatprueba.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map attributes) throws Exception
    {
        //System.out.println(request.getHeaders().getConnection());
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            System.out.println(servletRequest.getHeaders().toString());
            Arrays.stream(servletRequest.getServletRequest()
                    .getCookies())
                    .forEach(x->System.out.println(x.getName()+": "+x.getValue()));

            HttpSession session = servletRequest.getServletRequest().getSession();
            //System.out.println(session.getId());
            System.out.println(request.getPrincipal());
            attributes.put("sessionId", session.getId());
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
    }
}
