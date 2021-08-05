package com.jose.chatprueba.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2083153308365259001L;

    public UsuarioNotFoundException(Integer id) {
        super("No se ha encontrado el usuario con id " + id);
    }
}
