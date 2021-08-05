package com.jose.chatprueba.error;

import com.jose.chatprueba.exceptions.UsuarioConPasswordsDistintasException;
import com.jose.chatprueba.exceptions.UsuarioNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    //Excepción "comodín"
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {
        ApiError apiError = new ApiError(status, ex.getMessage());
        return ResponseEntity.status(status).headers(headers).body(apiError);
    }

    //Usuario no encontrado
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiError> handleUsuarioNoEncontrado(UsuarioNotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    //Contraseñas no coinciden
    @ExceptionHandler(UsuarioConPasswordsDistintasException.class)
    public ResponseEntity<ApiError> buildErrorResponseEntity(HttpStatus status, String message){
        return ResponseEntity.status(status)
                .body(ApiError.builder().mensaje(message).build());
    }

}
