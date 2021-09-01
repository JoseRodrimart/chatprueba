package com.jose.chatprueba.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jose.chatprueba.error.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, authException.getMessage());
        String strApiError = objectMapper.writeValueAsString(apiError);

        response.getWriter().println(strApiError);
    }
}
